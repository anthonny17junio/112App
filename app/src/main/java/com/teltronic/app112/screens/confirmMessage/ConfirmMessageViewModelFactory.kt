package com.teltronic.app112.screens.confirmMessage

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.classes.enums.Subcategory

class ConfirmMessageViewModelFactory(
    private val subcategory: Subcategory,
    private val activity: FragmentActivity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfirmMessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfirmMessageViewModel(subcategory, activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}