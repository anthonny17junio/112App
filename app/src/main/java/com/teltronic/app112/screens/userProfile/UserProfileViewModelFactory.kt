package com.teltronic.app112.screens.userProfile

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserProfileViewModelFactory(private val activity: FragmentActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}