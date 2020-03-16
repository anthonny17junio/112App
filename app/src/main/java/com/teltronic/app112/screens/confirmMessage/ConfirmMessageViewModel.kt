package com.teltronic.app112.screens.confirmMessage

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.classes.enums.Subcategory

class ConfirmMessageViewModel(subcat: Subcategory, activity: Activity) : ViewModel() {

    private val _strCategory = MutableLiveData<String>()
    val strCategory: LiveData<String>
        get() = _strCategory

    private val _subcategory = MutableLiveData<Subcategory>()
    val subcategory: LiveData<Subcategory>
        get() = _subcategory

    private val _boolNavigateToChat = MutableLiveData<Boolean>()
    val boolNavigateToChat: LiveData<Boolean>
        get() = _boolNavigateToChat

    init {
        _subcategory.value = subcat
        val category = subcat.category
        if (category != null) {
            _strCategory.value = activity.resources.getString(category.idTitle)
        } else {
            _strCategory.value = ""
        }

        _boolNavigateToChat.value = false
    }

    fun navigateToChat() {
        _boolNavigateToChat.value = true
    }

    fun navigateToChatComplete() {
        _boolNavigateToChat.value = false
    }

}