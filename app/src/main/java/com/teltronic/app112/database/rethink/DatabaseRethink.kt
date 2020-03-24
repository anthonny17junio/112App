package com.teltronic.app112.database.rethink

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection

abstract class DatabaseRethink {
    //Permite acceder a los métodos para crear u obtener la base de datos sin instanciar la clase
    companion object {
        //Volatile asgura que el valor de INSTANCE siempre esté actualizado en todos los hilos de ejecución
        //(un cambio en un hilo de ejecución será visible en todos los demás hilos)
        @Volatile
        private var CONNECTION: Connection? = null

        //Con esto se evita inicializar la BD varias veces, lo cual es costoso
        fun getConnection(): Connection {
            //synchronized asegura de que solo un hilo a la vez acceda a este bloque de código
            synchronized(this) {
                var connection = CONNECTION

                //rethinkdb.exe --bind all --initial-password "123456789"
                if (connection == null) {
                    val db = RethinkDB.r
                    //Aquí controlar que pasa si no hay internet
                    connection = db.connection().hostname("192.168.43.210").port(28015)
                        .user("admin", "123456789").connect()
                    CONNECTION = connection
                }
                requireNotNull(connection) // TODO - Qué pasa si no hay internet? está bien este not null aquí o debería ir en otro lugar
                return connection
            }
        }
    }
}