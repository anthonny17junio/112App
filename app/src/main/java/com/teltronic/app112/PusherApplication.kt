package com.teltronic.app112

import android.app.Application
import timber.log.Timber

class PusherApplication : Application(){ //Clase 4 lección 5
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()) //Inicializo Timber
    }
}