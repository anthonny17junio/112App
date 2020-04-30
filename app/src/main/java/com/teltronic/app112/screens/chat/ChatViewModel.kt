package com.teltronic.app112.screens.chat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.MessageType
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.rethink.tb_messages.MessagesRethink
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.DatabaseRoomHelper
import com.teltronic.app112.database.room.chats.ChatEntity
import com.teltronic.app112.database.room.chats.ChatWithMessages
import com.teltronic.app112.database.room.messages.MessageEntityConverter
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class ChatViewModel(
    application: Application,
    idChat: String,
    fragment: ChatFragment
) : AndroidViewModel(application) {
    lateinit var chat: LiveData<ChatWithMessages>

    private val _idColorResource = MutableLiveData<Int>()
    val idColorResource: LiveData<Int>
        get() = _idColorResource

    private val _idChat = MutableLiveData<String>()

    private val _subcategory = MutableLiveData<Subcategory>()
    val subcategory: LiveData<Subcategory>
        get() = _subcategory

    private val _chatState = MutableLiveData<ChatState>()
    val chatState: LiveData<ChatState>
        get() = _chatState

    private val _strLocation = MutableLiveData<String>()
    val strLocation: LiveData<String>
        get() = _strLocation

    private val _strDate = MutableLiveData<String>()
    val strDate: LiveData<String>
        get() = _strDate

    private val _strTime = MutableLiveData<String>()
    val strTime: LiveData<String>
        get() = _strTime

    private val _footerVisibility = MutableLiveData<Int>()
    val footerVisibility: LiveData<Int>
        get() = _footerVisibility

    private val _clearMessage = MutableLiveData<Boolean>()
    val clearMessage: LiveData<Boolean>
        get() = _clearMessage

    private val _startChatObserver = MutableLiveData<Boolean>()
    val startChatObserver: LiveData<Boolean>
        get() = _startChatObserver

    private val _startSendMessage = MutableLiveData<Boolean>()
    val startSendMessage: LiveData<Boolean>
        get() = _startSendMessage

    private val _fragment = fragment
    private val dataSourceChats = DatabaseApp.getInstance(application).chatsDao
    private val dataSourceMessages = DatabaseApp.getInstance(application).messagesDao

    private var _boolEnableInterface = MutableLiveData<Boolean>()
    val boolEnableInterface: LiveData<Boolean>
        get() = _boolEnableInterface

    private val _strErrorSendMessage = MutableLiveData<String>()
    val strErrorSendMessage: LiveData<String>
        get() = _strErrorSendMessage

    private var _idUser: String? = null


    init {
        enableInterface()
        _subcategory.value = Subcategory.OTHER
        _chatState.value = ChatState.IN_PROGRESS
        _idChat.value = idChat
        _idColorResource.value = ChatState.IN_PROGRESS.idColor
        _footerVisibility.value = View.GONE
        _strErrorSendMessage.value = ""
        initUI(idChat)
        _clearMessage.value = false
        _startChatObserver.value = false
        _startSendMessage.value = false
    }


    private fun initUI(idChat: String) {
        _fragment.uiScope.launch {
            initIO(idChat)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private suspend fun initIO(idChat: String) {
        withContext(Dispatchers.IO) {
            chat = dataSourceChats.getChatWithMessages(idChat)
            _startChatObserver.postValue(true)

            syncRoomRethinkMessages(idChat)
        }
    }

    private fun syncRoomRethinkMessages(idChat: String) {
        val con = DatabaseRethink.getConnection()
        if (con != null) {
            val rethinkMessages = MessagesRethink.getMessages(con, idChat)
            for (message in rethinkMessages) {
                val idMessage = message["id"] as String
                val messageRoom = dataSourceMessages.get(idMessage)
                if (messageRoom == null) {
                    insertMessageInRoom(message)
                }
            }
        }
    }

    private fun insertMessageInRoom(message: HashMap<*, *>) {
        val context = _fragment.context
        if (context != null) {
            val hshMessage = message as HashMap<String, *>
            val messageRoom = MessageEntityConverter.fromHashMap(hshMessage)
            if (messageRoom != null) {
                dataSourceMessages.insert(messageRoom)
            }
        }
    }


//    private fun updateMessages(messages: List<MessageEntity>) {
//        _fragment.adapter.submitMessagesList(messages)
//    }

    @SuppressLint("SimpleDateFormat")
    fun updateHeader(chat: ChatEntity) {
        val chatState = ChatState.getById(chat.id_chat_state)!!
        val sdfDate = SimpleDateFormat("dd-MM-yyyy")
        val sdfHour = SimpleDateFormat("HH:mm")
        val creationDate = Date(chat.creation_epoch_time.toLong() * 1000)
        val color =
            ContextCompat.getColor(
                getApplication(),
                chatState.idColor
            )

        _subcategory.postValue(Subcategory.getById(chat.id_subcategory))
        _chatState.postValue(chatState)
        _idColorResource.postValue(color)
        _strDate.postValue(sdfDate.format(creationDate))
        _strTime.postValue(sdfHour.format(creationDate))
        _strLocation.postValue(chat.location)

        if (chatState.id == ChatState.IN_PROGRESS.id) {
            //Si el chat está en proceso, el footer (para escibir nuevos mensajes) es visible
            _footerVisibility.postValue(View.VISIBLE)
        } else {
            //Si el chat está finalizado, el footer (para escibir nuevos mensajes) desaparece
            _footerVisibility.postValue(View.GONE)
        }
    }

//    fun changeChatState() {
//        val idChat = _idChat.value
//        val chatState = _chatState.value
//        if (idChat != null && chatState != null) {
//            val idChatState = chatState.id
//            if (idChatState == ChatState.IN_PROGRESS.id) {
//                uiScope.launch {
//                    changeStateIO(idChat, ChatState.PROCESSED.id)
//                }
//            } else if (idChatState == ChatState.PROCESSED.id) {
//                uiScope.launch {
//                    changeStateIO(idChat, ChatState.IN_PROGRESS.id)
//                }
//            }
//        }
//    }

//    private suspend fun changeStateIO(idChat: String, idNewChatState: Int) {
//        withContext(Dispatchers.IO) {
//            dataSourceChats.changeState(idChat, idNewChatState)
//        }
//    }

    fun trySendMessage() {
        _startSendMessage.value = true
    }

    fun messageSent() {
        _startSendMessage.value = false
    }

    fun trySendMessageIO(message: String) {
        disableInterface()
        //Tener conexión con rethinkDB
        val con = DatabaseRethink.getConnection()
        if (con != null) {
            //Tener un id de usuario válido
            if (_idUser == null) {
                _idUser = DatabaseRoomHelper.getOrInsertSynchronizedRethinkId(
                    con,
                    _fragment.activity as Activity
                )
            }
            if (_idUser != null) {
                val idSentMessage = sendMessage(message)
                if (idSentMessage == null) {
                    _strErrorSendMessage.postValue(
                        (getApplication() as Application).getString(R.string.error_sending_message)
                    )
                } else {
                    _clearMessage.postValue(true)
                }
            } else {
                _strErrorSendMessage.postValue(
                    (getApplication() as Application).getString(R.string.error_getting_user)
                )
            }
        } else {
            _strErrorSendMessage.postValue(
                (getApplication() as Application).getString(R.string.no_database_connection)
            )
        }
        enableInterface()
    }

    fun messageCleared() {
        _clearMessage.value = false
    }

    private fun sendMessage(message: String): String? {
        val idUser = _idUser
        val idChat = _idChat.value
        requireNotNull(idUser)
        requireNotNull(idChat)

        val con = DatabaseRethink.getConnection()
        return if (con != null) {
            val idMessageType = MessageType.TEXT.id
            MessagesRethink.sendTextMessage(con, message, idUser, idChat, idMessageType)
        } else {
            null
        }
    }

    private fun disableInterface() {
        _boolEnableInterface.postValue(false)
    }

    private fun enableInterface() {
        _boolEnableInterface.postValue(true)
    }

    fun clearStrErrorSendMessage() {
        _strErrorSendMessage.value = ""
    }

}
