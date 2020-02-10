package com.teltronic.app112.screens.mainScreen

import androidx.lifecycle.ViewModel
import timber.log.Timber

class MainViewModel:ViewModel() {
    init {
        Timber.i("MainViewModel creado")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("MainViewModel destruido")
    }
}