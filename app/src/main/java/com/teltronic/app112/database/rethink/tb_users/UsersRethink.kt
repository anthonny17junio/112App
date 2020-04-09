@file:Suppress("UNCHECKED_CAST")

package com.teltronic.app112.database.rethink.tb_users

import com.rethinkdb.RethinkDB
import com.rethinkdb.gen.ast.ReqlExpr
import com.rethinkdb.net.Connection
import com.rethinkdb.net.Cursor
import com.teltronic.app112.classes.enums.NamesRethinkdb
import java.util.HashMap

object UsersRethink {
    private val r = RethinkDB.r
    private val table = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_USERS.text)

    //Obtiene el id de rethinkDB dado un id
    fun getIdUserById(con: Connection, id: String): String? {
        val cursor = table.filter { row: ReqlExpr -> row.g("id").eq(id) }
            .run<Cursor<HashMap<*, *>>>(con)

        val list = cursor.bufferedItems()
        for (item in list) {
            val jsonObject = item as org.json.simple.JSONObject
            return jsonObject["id"] as String
        }

        return null
    }

    //Obtiene el googleId de rethinkDB dado un id
    fun getGoogleIdUserById(con: Connection, id: String): String? {
        val cursor = table.filter { row: ReqlExpr -> row.g("id").eq(id) }
            .run<Cursor<HashMap<*, *>>>(con)

        val list = cursor.bufferedItems()
        for (item in list) {
            val jsonObject = item as org.json.simple.JSONObject
            val jsonElement = jsonObject["id_google"]
            if (jsonElement != null)
                return jsonElement as String
        }

        return null
    }


    //Obtiene el id de rethinkDB dado un googleId
    fun getIdUserByGoogleId(con: Connection, googleId: String): String? {
        val cursor = table.filter { row: ReqlExpr -> row.g("id_google").eq(googleId) }
            .run<Cursor<HashMap<*, *>>>(con)

        val list = cursor.bufferedItems()
//        if(list.size>1){
//            throw error("id_google should be unique in database") //¿REMOVE DUPLICATES?
//        }
        for (item in list) {
            val jsonObject = item as org.json.simple.JSONObject
            return jsonObject["id"] as String
        }

        return null
    }

    //Inserta un nuevo usuario que NO está autenticado con google y devuelve el id
    fun insertNewUserWithoutGoogleId(con: Connection): String {
        val itemInserted = table.insert(
                r.hashMap("creation_time", r.now())
                    .with("last_access", r.now())
            )
            .run<HashMap<*, *>>(con)

        return (itemInserted["generated_keys"] as ArrayList<String>)[0]
    }

    //Inserta un nuevo usuario que está autenticado con google y devuelve el id
    fun insertNewUserWithGoogleId(con: Connection, googleId: String): String {
        val itemInserted = table.insert(
                r.hashMap("id_google", googleId)
                    .with("creation_time", r.now())
                    .with("last_access", r.now())
            )
            .run<HashMap<*, *>>(con)

        return (itemInserted["generated_keys"] as ArrayList<String>)[0]
    }
}