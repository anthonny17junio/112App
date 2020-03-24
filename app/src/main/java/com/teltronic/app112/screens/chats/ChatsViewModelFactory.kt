package com.teltronic.app112.screens.chats

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.databinding.FragmentChatsBinding

class ChatsViewModelFactory (private val binding: FragmentChatsBinding,private val activity: Activity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatsViewModel(binding,activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}