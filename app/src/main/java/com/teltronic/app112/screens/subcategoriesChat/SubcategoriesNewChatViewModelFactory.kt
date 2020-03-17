package com.teltronic.app112.screens.subcategoriesChat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.classes.enums.Category

class SubcategoriesNewChatViewModelFactory(
    private val category: Category,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubcategoriesNewChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubcategoriesNewChatViewModel(category, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}