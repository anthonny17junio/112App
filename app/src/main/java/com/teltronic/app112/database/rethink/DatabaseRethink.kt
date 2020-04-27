package com.teltronic.app112.database.rethink

import com.rethinkdb.RethinkDB
import com.rethinkdb.gen.exc.ReqlDriverError
import com.rethinkdb.net.Connection
import com.teltronic.app112.classes.enums.NamesRethinkdb

abstract class DatabaseRethink {
    //Permite acceder a los métodos para crear u obtener la base de datos sin instanciar la clase
    companion object {
        //Volatile asgura que el valor de INSTANCE siempre esté actualizado en todos los hilos de ejecución
        //(un cambio en un hilo de ejecución será visible en todos los demás hilos)
        @Volatile
        private var CONNECTION: Connection? = null

        //Con esto se evita inicializar la BD varias veces, lo cual es costoso
        fun getConnection(): Connection? {
            //synchronized asegura de que solo un hilo a la vez acceda a este bloque de código
            synchronized(this) {
                var connection = CONNECTION

                //rethinkdb.exe --bind all --initial-password "123456789"
                if (connection == null) {
                    val r = RethinkDB.r
                    try {
                        connection = r.connection().hostname("192.168.43.210").port(28015)
                            .user("admin", "123456789").connect()
                    } catch (e: ReqlDriverError) {
                        //No se puede establecer la conexión
                        return null
                    }
                    CONNECTION = connection

                    //TODO - BORRAR ESTO, NO DEBERÍA ESTAR AQUÍ (SOLO SE DEBERÍA EJECUTAR UNA VEZ Y NUNCA MÁS)
                    verifyAllDatabase(r, connection)
                }
                return connection!!
            }
        }

        private fun verifyAllDatabase(r: RethinkDB, con: Connection) {
//            r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text).delete().run<Any>(con) //Elimina la tabla chats
//            r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_USERS.text).delete().run<Any>(con) //Elimina la tabla users
//            r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text).delete().run<Any>(con) //Elimina la tabla users
//            r.dbDrop(NamesRethinkdb.DATABASE.text).run<Any>(con) //Elimina la base de datos
            verifyDatabase(r, con)
            verifyTbUsers(r, con)
            verifyTbChats(r, con)
            verifyTbMessages(r,con)
        }

        private fun verifyDatabase(r: RethinkDB, con: Connection) {
            val dataBases = r.dbList().run(con) as ArrayList<String>

            var existe = false
            for (dataBase in dataBases) {
                if (dataBase == NamesRethinkdb.DATABASE.text) {
                    existe = true
                    break
                }
            }

            if (!existe) {
                r.dbCreate(NamesRethinkdb.DATABASE.text).run<Any>(con)
            }
        }
        private fun verifyTbMessages(r: RethinkDB, con: Connection) {
            val tables =
                r.db(NamesRethinkdb.DATABASE.text).tableList().run(con) as ArrayList<String>

            var existe = false
            for (table in tables) {
                if (table == NamesRethinkdb.TB_MESSAGES.text) {
                    existe = true
                    break
                }
            }

            if (!existe) {
                r.db(NamesRethinkdb.DATABASE.text).tableCreate(NamesRethinkdb.TB_MESSAGES.text)
                    .run(con) as Any
                //Crea el índice
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
                    .indexCreate("creation_time").run(con) as Any
                //Espera a que el índice esté creado para ser usado
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
                    .indexWait("creation_time").run(con) as Any
                //Crea el índice
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
                    .indexCreate("id_user").run(con) as Any
                //Espera a que el índice esté creado para ser usado
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
                    .indexWait("id_user").run(con) as Any
                //Crea el índice
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
                    .indexCreate("id_chat").run(con) as Any
                //Espera a que el índice esté creado para ser usado
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
                    .indexWait("id_chat").run(con) as Any
            }
        }

        private fun verifyTbUsers(r: RethinkDB, con: Connection) {
            val tables =
                r.db(NamesRethinkdb.DATABASE.text).tableList().run(con) as ArrayList<String>

            var existe = false
            for (table in tables) {
                if (table == NamesRethinkdb.TB_USERS.text) {
                    existe = true
                    break
                }
            }

            if (!existe) {
                r.db(NamesRethinkdb.DATABASE.text).tableCreate(NamesRethinkdb.TB_USERS.text)
                    .run(con) as Any
            }
        }

        private fun verifyTbChats(r: RethinkDB, con: Connection) {
            val tables =
                r.db(NamesRethinkdb.DATABASE.text).tableList().run(con) as ArrayList<String>

            var existe = false
            for (table in tables) {
                if (table == NamesRethinkdb.TB_CHATS.text) {
                    existe = true
                    break
                }
            }

            if (!existe) {
                r.db(NamesRethinkdb.DATABASE.text).tableCreate(NamesRethinkdb.TB_CHATS.text)
                    .run(con) as Any
                //Crea el índice
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                    .indexCreate("creation_time").run(con) as Any
                //Espera a que el índice esté creado para ser usado
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                    .indexWait("creation_time").run(con) as Any
                //Crea el índice
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                    .indexCreate("id_user").run(con) as Any
                //Espera a que el índice esté creado para ser usado
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                    .indexWait("id_user").run(con) as Any
            }
        }

    }
}