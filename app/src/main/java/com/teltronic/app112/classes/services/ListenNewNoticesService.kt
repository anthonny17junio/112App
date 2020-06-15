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
import com.rethinkdb.model.OptArgs
import com.rethinkdb.net.Cursor
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.DistanceValues
import com.teltronic.app112.classes.enums.NamesRethinkdb
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.notices.NoticeEntityConverter
import com.teltronic.app112.screens.MainActivity
import kotlinx.coroutines.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Suppress("UNCHECKED_CAST", "SENSELESS_COMPARISON")
class ListenNewNoticesService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var currentLat: Double? = null
    private var currentLong: Double? = null
    private var distanceId: Int? = null

    private val databaseApp = DatabaseApp.getInstance(this)
    private val dataSourceNotices = databaseApp.noticesDao

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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        uiScope.launch {
            val extras = intent.extras
            if (extras != null) {
                currentLat = extras.get("latNotices") as Double?
                currentLong = extras.get("longNotices") as Double?
                distanceId = extras.get("distanceId") as Int?
            }
            listenNewNoticesIO()
        }
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }

    private lateinit var cursorChangesTbNotices: Cursor<*>
    private suspend fun listenNewNoticesIO() {
        withContext(Dispatchers.IO) {

//            var i = 0
//            while (true) {
//                Thread.sleep(500)
//                Log.e("Service notices", i.toString())
//                i++
//            }

            val con = DatabaseRethink.getConnection()
            val r = RethinkDB.r
            val tableNotices =
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_NOTICES.text)

            if (con != null) {
                cursorChangesTbNotices = tableNotices.changes()
                    .run(con, OptArgs.of("time_format", "raw")) as Cursor<*>

                for (change in cursorChangesTbNotices) { //Esto se ejecutará cada vez que haya un cambio en la tabla "tb_chats"
                    if (job.isActive) {
                        val cambio = change as HashMap<String, HashMap<String, *>>

                        var isMyNotification = true
                        if (distanceId != null) {
                            when (val distance = DistanceValues.getById(distanceId!!)) {
                                DistanceValues.NONE_KM ->
                                    isMyNotification = false
                                else -> {
                                    if (distance != DistanceValues.NO_LIMIT) {
                                        //ROOM LOCATION = (currentLat, currentLong)
                                        val latNotice = (cambio["new_val"])?.get("lat") as Double
                                        val longNotice = (cambio["new_val"])?.get("long") as Double
                                        val distanceRequired = distance!!.valueKm

                                        val distanceBetweenTwoPoints = getDistanceBetweenTwoPoints(
                                            currentLat!!,
                                            currentLong!!,
                                            latNotice,
                                            longNotice
                                        )
                                        if (distanceBetweenTwoPoints > distanceRequired)
                                            isMyNotification = false
                                    }
                                }

                            }
                        }

                        if (isMyNotification) {
                            val newNotice = NoticeEntityConverter.fromHashMap(cambio["new_val"])

                            val idNotice = newNotice?.id
                            val strTitle = newNotice?.title
                            val strText = newNotice?.message
                            //aquí se debería extraer la imagen
                            if (idNotice != null) {
                                //Store notice in room
                                sendNotification(idNotice, strTitle!!, strText!!)
                                dataSourceNotices.insert(newNotice)
                            }
                        }
                    } else {
                        cursorChangesTbNotices.close()
                    }
                }
            }
        }
    }

    private fun getDistanceBetweenTwoPoints(
        lat1: Double,
        long1: Double,
        lat2: Double,
        long2: Double
    ): Int {
        val earthRadiusKm = 6371

        val dLat = degreesToRadians(lat2 - lat1)
        val dLon = degreesToRadians(long2 - long1)

        val radLat1 = degreesToRadians(lat1)
        val radLat2 = degreesToRadians(lat2)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(radLat1) * cos(radLat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadiusKm * c).toInt()
    }

    private fun degreesToRadians(degrees: Double): Double {
        return degrees * Math.PI / 180
    }

    private fun sendNotification(idNotice: String, titleNotice: String, textNotice: String) {
        val configurations = DatabaseApp.getInstance(applicationContext).userRethinkDao
        if (configurations != null) {
            val arguments = Bundle()
            arguments.putString("idNotice", idNotice)

            val pendingIntent: PendingIntent = NavDeepLinkBuilder(applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.navigation)
                .setDestination(R.id.chatFragment)
                .setArguments(arguments)
                .createPendingIntent()

            val builder = NotificationCompat.Builder(applicationContext, "idChannel")
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(titleNotice)
                .setContentText(textNotice)
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