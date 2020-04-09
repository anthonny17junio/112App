package com.teltronic.app112.screens.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatsViewModelFactory (private val fragment: ChatsFragment): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatsViewModel(fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}