package com.teltronic.app112.screens.confirmMessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.classes.Subcategory

class ConfirmMessageViewModelFactory(private val subcategory: Subcategory): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfirmMessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfirmMessageViewModel(subcategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}