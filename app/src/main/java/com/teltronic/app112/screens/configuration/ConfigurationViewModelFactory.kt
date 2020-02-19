package com.teltronic.app112.screens.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConfigurationViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfigurationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfigurationViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}