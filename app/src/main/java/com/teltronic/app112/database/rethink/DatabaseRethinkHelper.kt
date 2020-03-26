package com.teltronic.app112.database.rethink

import com.rethinkdb.RethinkDB
import kotlinx.coroutines.*

object DatabaseRethinkHelper {


    //**********************************************************************************************
    //tb_users
    //**********************************************************************************************
//    //Crea tb_users
//    fun createTbUsers(uiScope: CoroutineScope) {
//        uiScope.launch {
//            createTbUsersIO()
//        }
//    }
//
//    private suspend fun createTbUsersIO() {
//        withContext(Dispatchers.IO) {
//            val con = DatabaseRethink.getConnection()
//            val r = RethinkDB.r
//            r.db("app112db").tableCreate("tb_users").run(con) as Any
//            //Crea el índice
//            r.table("tb_users").indexCreate("creation_time").run(con) as Any
//            //Espera a que el índice esté creado para ser usado
//            r.table("tb_users").indexWait("creation_time").run(con) as Any
//        }
//    }
//
    //**********************************************************************************************
    //Fin tb_users
    //**********************************************************************************************

    //**********************************************************************************************
    //tb_palabras
    //**********************************************************************************************
//    //Crea tb_plabras
//    fun createTbPalabras(uiScope: CoroutineScope) {
//        uiScope.launch {
//            createTbPalabrasIO()
//        }
//    }
//
//    private suspend fun createTbPalabrasIO() {
//        withContext(Dispatchers.IO) {
//            val con = DatabaseRethink.getConnection()
//            val r = RethinkDB.r
//            r.db("test").tableCreate("tb_palabras").run(con) as Any
//            //Crea el índice
//            r.table("tb_palabras").indexCreate("time").run(con) as Any
//            //Espera a que el índice esté creado para ser usado
//            r.table("tb_palabras").indexWait("time").run(con) as Any
//        }
//    }
//
//    //Elimina tb_palabras
//    fun removeTbPalabras(uiScope: CoroutineScope) {
//        uiScope.launch {
//            removeTbPalabrasIO()
//        }
//    }
//
//    private suspend fun removeTbPalabrasIO() {
//        withContext(Dispatchers.IO) {
//            val con = DatabaseRethink.getConnection()
//            val r = RethinkDB.r
//            r.db("test").tableDrop("tb_palabras").run(con) as Any
//        }
//    }
//
    //Elimina todos los documentos de tb_palabras
    fun removeAllDocumentsTbPalabras(uiScope: CoroutineScope) {
        uiScope.launch {
            removeAllDocumentsTbPalabrasIO()
        }
    }

    private suspend fun removeAllDocumentsTbPalabrasIO() {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val r = RethinkDB.r
            r.table("tb_palabras").delete().run(con) as Any
        }
    }
    //**********************************************************************************************
    //Fin tb_palabras
    //**********************************************************************************************
}