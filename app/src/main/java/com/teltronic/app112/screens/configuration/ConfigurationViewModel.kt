package com.teltronic.app112.screens.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {
    private var _boolSave = MutableLiveData<Boolean>()
    val boolSave: LiveData<Boolean>
        get() = _boolSave

    fun save() {
        _boolSave.value = true
    }

    fun saveComplete() {
        _boolSave.value = false
    }

    init {
        _boolSave.value = false
    }
}