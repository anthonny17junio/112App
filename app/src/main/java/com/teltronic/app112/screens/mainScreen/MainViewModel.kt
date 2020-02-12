package com.teltronic.app112.screens.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    //PUBLIC AND PRIVATE VARIABLES
    //****************************************************
    //Navigate to notices
    private var _boolNavigateToNotices = MutableLiveData<Boolean>()
    val boolNavigateToNotices: LiveData<Boolean>
        get() = _boolNavigateToNotices

    //Navigate to location
    private var _boolNavigateToLocation = MutableLiveData<Boolean>()
    val boolNavigateToLocation: LiveData<Boolean>
        get() = _boolNavigateToLocation

    //Navigate to chats
    private var _boolNavigateToChats = MutableLiveData<Boolean>()
    val boolNavigateToChats: LiveData<Boolean>
        get() = _boolNavigateToChats

    //Navigate to new chat
    private var _boolNavigateToNewChat = MutableLiveData<Boolean>()
    val boolNavigateToNewChat: LiveData<Boolean>
        get() = _boolNavigateToNewChat


    //INIT
    //****************************************************
    init {
        _boolNavigateToNotices.value = false
        _boolNavigateToLocation.value = false
        _boolNavigateToChats.value = false
        _boolNavigateToNewChat.value = false
    }

    //FUNCTIONS
    //****************************************************
    //Navigate to notices
    fun navigateToNotices() {
        _boolNavigateToNotices.value = true
    }

    fun onNavigateToNoticesComplete() {
        _boolNavigateToNotices.value = false
    }

    //Navigate to location
    fun navigateToLocation() {
        _boolNavigateToLocation.value = true
    }

    fun onNavigateToLocationComplete() {
        _boolNavigateToLocation.value = false
    }

    //Navigate to chats
    fun navigateToChats() {
        _boolNavigateToChats.value = true
    }

    fun navigateToChatsComplete() {
        _boolNavigateToChats.value = false
    }

    //Navigate to new chat
    fun navigateToNewChat() {
        _boolNavigateToNewChat.value = true
    }

    fun navigateToNewChatComplete() {
        _boolNavigateToNewChat.value = false
    }

}
