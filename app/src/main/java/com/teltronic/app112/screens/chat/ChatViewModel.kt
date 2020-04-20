package com.teltronic.app112.screens.chat

import android.annotation.SuppressLint
import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.room.DatabaseApp
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel(
    application: Application,
    idChat: String
) : AndroidViewModel(application) {
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

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val dataSourceChats = DatabaseApp.getInstance(application).chatsDao

    init {
        _subcategory.value = Subcategory.OTHER
        _chatState.value = ChatState.IN_PROGRESS
        _idChat.value = idChat
        _idColorResource.value = ChatState.IN_PROGRESS.idColor

        uiScope.launch {
            initIO(idChat)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private suspend fun initIO(idChat: String) {
        withContext(Dispatchers.IO) {
            val chatWithMessages = dataSourceChats.getChatWithMessages(idChat).value
            val chat = chatWithMessages?.chat
            val messages = chatWithMessages?.messages
            requireNotNull(chat)
            val sdfDate = SimpleDateFormat("dd-MM-yyyy")
            val sdfHour = SimpleDateFormat("HH:mm")
            val creationDate = Date(chat.creation_epoch_time.toLong() * 1000)
            val color = ContextCompat.getColor(getApplication(), _chatState.value!!.idColor)

            _subcategory.postValue(Subcategory.getById(chat.id_subcategory))
            _chatState.postValue(ChatState.getById(chat.id_chat_state))
            _idColorResource.postValue(color)
            _strDate.postValue(sdfDate.format(creationDate))
            _strTime.postValue(sdfHour.format(creationDate))
            _strLocation.postValue(chat.location)

        }
    }
}
