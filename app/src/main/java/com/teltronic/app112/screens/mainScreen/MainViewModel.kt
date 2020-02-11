package com.teltronic.app112.screens.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var _boolNavigateToLocation = MutableLiveData<Boolean>()
    val boolNavigateToLocation: LiveData<Boolean>
        get() = _boolNavigateToLocation

    init {
        _boolNavigateToLocation.value = false
    }

    fun navigateToLocation() {
        _boolNavigateToLocation.value = true
    }

    fun onNavigateToLocationComplete(){
        _boolNavigateToLocation.value = false
    }
}