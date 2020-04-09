package com.teltronic.app112.screens.chats

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rethinkdb.net.Connection
import com.teltronic.app112.R
import com.teltronic.app112.classes.ConnectionLiveData
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.rethink.tb_chats.ChatsRethink
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.DatabaseRoomHelper
import com.teltronic.app112.database.room.chats.ChatEntity
import kotlinx.coroutines.*
import org.json.simple.JSONObject
import kotlin.collections.HashMap


class ChatsViewModel(fragment: ChatsFragment) :
    ViewModel() {

    private val _strError = MutableLiveData<String>()
    val strError: LiveData<String>
        get() = _strError

    lateinit var chats: LiveData<List<ChatEntity>>

    private val _networkConnection = ConnectionLiveData(fragment.context)
    private var _rethinkDBConnection: Connection? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _fragment = fragment
    private val dataSource = DatabaseApp.getInstance(_fragment.activity!!.application).chatsDao

    init {
        getChatsRoom()
        _networkConnection.observe(fragment as LifecycleOwner,
            Observer { isNetworkConnected ->
                if (isNetworkConnected) {
                    uiScope.launch {
                        if (_rethinkDBConnection == null) {
                            _strError.value = fragment.getString(R.string.no_database_connection)
                        }
                        getRethinkdbConnectionIO()
                    }
                } else {
                    _strError.value = fragment.getString(R.string.no_network_connection)
                }
            }
        )
    }

    private fun getChatsRoom() {
        chats = dataSource.getAll()
    }

    private fun clearStrErrorCreateChat() {
        _strError.postValue("")
    }

    private suspend fun getRethinkdbConnectionIO() {
        withContext(Dispatchers.IO) {
            _rethinkDBConnection = DatabaseRethink.getConnection()

            if (_rethinkDBConnection != null) {
                clearStrErrorCreateChat()
                syncRoomRethinkChats()
            }
        }
    }

    private fun syncRoomRethinkChats() {
        val con = DatabaseRethink.getConnection()
        requireNotNull(con)
        val userId =
            DatabaseRoomHelper.getOrInsertSynchronizedRethinkId(con, _fragment.activity as Activity)
        requireNotNull(userId)
        val chats = ChatsRethink.getChatsByUser(con, userId)
        for (chat in chats) {
            val idChat = chat["id"] as String
            val chatRoom = dataSource.get(idChat)
            if (chatRoom == null) {
                insertChatInRoom(chat)
            }
        }
    }

    private fun insertChatInRoom(chat: HashMap<*, *>) {
        val id = chat["id"] as String
        val creationTime = chat["creation_time"] as JSONObject
        val creationTimezone = creationTime["timezone"] as String
        val creationEpochTime = creationTime["epoch_time"] as Double

        val idSubcategory = (chat["id_subcategory"] as Long).toInt()
        val idChatState = (chat["id_chat_state"] as Long).toInt()
        val lat = chat["lat"] as Double
        val long = chat["long"] as Double
        val lastLanObj = chat["last_lat"]
        val lastLongObj = chat["last_long"]
        val realTime = !(lastLanObj == null || lastLongObj == null)

        if (_fragment.context != null) {
            val cityName = Phone.getCityName(_fragment.context!!, lat, long)

            val chatInsert = ChatEntity(
                id,
                idSubcategory,
                idChatState,
                creationEpochTime,
                creationTimezone,
                cityName,
                realTime
            )

            dataSource.insert(chatInsert)
        }
    }
}
