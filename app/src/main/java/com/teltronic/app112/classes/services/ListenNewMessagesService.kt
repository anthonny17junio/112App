@file:Suppress("KotlinDeprecation")

package com.teltronic.app112.classes.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.rethinkdb.RethinkDB
import com.rethinkdb.gen.ast.ReqlExpr
import com.rethinkdb.model.OptArgs
import com.rethinkdb.net.Cursor
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.MessageType
import com.teltronic.app112.classes.enums.NamesRethinkdb
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.messages.MessageEntityConverter
import com.teltronic.app112.screens.MainActivity
import kotlinx.coroutines.*

@Suppress("UNCHECKED_CAST", "SENSELESS_COMPARISON")
class ListenNewMessagesService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var idUser: String

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        uiScope.launch {
            val extras = intent.extras
            idUser = extras!!.get("idUser") as String
            listenNewMessagesIO()
        }
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private lateinit var cursorChangesTbMessages: Cursor<*>
    private suspend fun listenNewMessagesIO() {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val r = RethinkDB.r
            val tableChats = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                .getAll(idUser).optArg("index", "id_user")
            val tableMessages =
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)

            if (con != null) {
                cursorChangesTbMessages = tableMessages.changes().filter { change: ReqlExpr ->
                    (r.expr(tableChats.g("id").coerceTo("array")))
                        .contains(change.g("new_val").g("id_chat"))
                }.filter { change: ReqlExpr -> (change.g("new_val").g("id_user").ne(idUser)) }
                    .run(con, OptArgs.of("time_format", "raw")) as Cursor<*>
                for (change in cursorChangesTbMessages) { //Esto se ejecutará cada vez que haya un cambio en la tabla "tb_chats"
                    if (job.isActive) {
                        val cambio = change as HashMap<String, HashMap<String, *>>

                        val newMessage = MessageEntityConverter.fromHashMap(
                            cambio["new_val"]
                        )
                        val idChat = newMessage?.id_chat
                        val idType = newMessage?.id_message_type
                        val strMessage = if (idType == MessageType.TEXT.id) {
                            newMessage.content
                        } else {
                            getString(R.string.picture)
                        }
                        if (idChat != null)
                            sendNotification(idChat, strMessage)
                    } else {
                        cursorChangesTbMessages.close()
                    }
                }
            }
        }
    }

    private fun sendNotification(idChat: String, message: String) {
        val configurations = DatabaseApp.getInstance(applicationContext).userRethinkDao
        if (configurations != null) {
            val arguments = Bundle()
            arguments.putString("idChat", idChat)
            arguments.putString("idUserRoom", configurations.get().id_rethink)

            val pendingIntent: PendingIntent = NavDeepLinkBuilder(applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.navigation)
                .setDestination(R.id.chatFragment)
                .setArguments(arguments)
                .createPendingIntent()

            val builder = NotificationCompat.Builder(applicationContext, "idChannel")
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(getString(R.string.notification_new_message))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val rnd = (0..10000).random()
            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(rnd, builder.build())
            }
        }
    }

}