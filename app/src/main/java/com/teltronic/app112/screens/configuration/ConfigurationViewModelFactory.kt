package com.teltronic.app112.screens.configuration

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConfigurationViewModelFactory(private var activity: FragmentActivity?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfigurationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfigurationViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}