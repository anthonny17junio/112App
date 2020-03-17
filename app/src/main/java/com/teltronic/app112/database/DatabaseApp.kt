package com.teltronic.app112.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teltronic.app112.database.medicalInfo.MedicalInfoDao
import com.teltronic.app112.database.medicalInfo.MedicalInfoEntity

/*
En entities van todas las tablas (entities)  de la base de datos
exportSchema es true por defecto
 */
@Database(entities = [MedicalInfoEntity::class], version = 1, exportSchema = false)
abstract class DatabaseApp : RoomDatabase() {
    abstract val medicalInfoDao: MedicalInfoDao

    companion object {
        //Permite acceder a los métodos para crear u obtener la base de datos sin instanciar la clase

        //Volatile asgura que el valor de INSTANCE siempre esté actualizado en todos los hilos de ejecución
        //(un cambio en un hilo de ejecución será visible en todos los demás hilos)
        @Volatile
        private var INSTANCE: DatabaseApp? = null //Con esto se evita inicializar la BD varias veces, lo cual es costoso

        fun getInstance(context: Context): DatabaseApp {
            synchronized(this) {
                //Asegura de que solo un hilo a la vez acceda a este bloque de código
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseApp::class.java,
                        "database_112"
                    ).fallbackToDestructiveMigration().build()
                    //información sobre migration
                    //https://developer.android.com/training/data-storage/room/migrating-db-versions
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}