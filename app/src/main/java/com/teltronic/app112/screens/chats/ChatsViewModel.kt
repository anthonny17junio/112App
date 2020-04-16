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
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.rethink.tb_chats.ChatsRethink
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.DatabaseRoomHelper
import com.teltronic.app112.database.room.chats.ChatEntity
import com.teltronic.app112.database.room.chats.ChatEntityConverter
import kotlinx.coroutines.*
import kotlin.collections.HashMap


@Suppress("UNCHECKED_CAST")
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

    private val _navigateToChat = MutableLiveData<String>()
    val navigateToChat
        get() = _navigateToChat

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

    fun onChatClicked(id: String) {
        _navigateToChat.value = id
    }

    fun onChatNavigated(){
        _navigateToChat.value = null
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

    //Inserta algún chat en el caso de no existir en Room
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
        val context = _fragment.context
        if (context != null) {
            val hshChat = chat as HashMap<String, *>
            val chatInsert = ChatEntityConverter.fromHashMap(hshChat, context)

            if (chatInsert != null)
                dataSource.insert(chatInsert)
        }
    }
}
