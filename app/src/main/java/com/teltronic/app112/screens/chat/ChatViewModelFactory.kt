package com.teltronic.app112.screens.chat

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory

class ChatViewModelFactory(private var fragment: Fragment, private var subCategory: Subcategory, private var chatState: ChatState) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(fragment, subCategory, chatState, null) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}