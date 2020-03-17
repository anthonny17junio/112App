package com.teltronic.app112.screens.chat

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory

class ChatViewModel(
    application: Application,
    subcategory: Subcategory,
    chatState: ChatState,
    idChat: Int?
) : AndroidViewModel(application) {
    private val _idColorResource = MutableLiveData<Int>()
    val idColorResource: LiveData<Int>
        get() = _idColorResource

    private val _idChat = MutableLiveData<Int>()
    val idChat: LiveData<Int>
        get() = _idChat

    private val _subcategory = MutableLiveData<Subcategory>()
    val subcategory: LiveData<Subcategory>
        get() = _subcategory

    private val _chatState = MutableLiveData<ChatState>()
    val chatState: LiveData<ChatState>
        get() = _chatState

    init {
        _subcategory.value = subcategory
        _chatState.value = chatState
        _idChat.value = idChat //Cuando idChat sea diferente de null traer los chats existentes
        _idColorResource.value = ContextCompat.getColor(application, _chatState.value!!.idColor)
    }
}
