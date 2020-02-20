package com.teltronic.app112.screens.location

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocationViewModelFactory(private var activity: FragmentActivity?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}