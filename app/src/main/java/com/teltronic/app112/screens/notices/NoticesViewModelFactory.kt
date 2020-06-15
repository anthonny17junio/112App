package com.teltronic.app112.screens.notices

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoticesViewModelFactory(private var application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoticesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}