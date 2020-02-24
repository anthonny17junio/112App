package com.teltronic.app112.screens.subcategoriesChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.classes.Category

class SubcategoriesNewChatViewModelFactory(private val category: Category): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubcategoriesNewChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubcategoriesNewChatViewModel(category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}