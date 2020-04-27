@file:Suppress("UNCHECKED_CAST")

package com.teltronic.app112.database.rethink.tb_chats

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import com.rethinkdb.net.Cursor
import com.teltronic.app112.classes.enums.NamesRethinkdb
import java.lang.Exception
import java.util.HashMap

object ChatsRethink {
    private val r = RethinkDB.r
    private val table = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)

    //Inserta un nuevo chat y devuelve el id
    fun insertNewChat(
        con: Connection,
        userId: String,
        subcategoryId: Int,
        lat: Double,
        long: Double,
        realTime: Boolean,
        chatStateId: Int
    ): String? {
        val itemToInsert = if (realTime) {
            r.hashMap("creation_time", r.now())
                .with("id_user", userId)
                .with("id_subcategory", subcategoryId)
                .with("lat", lat)
                .with("long", long)
                .with("last_lat", lat)
                .with("last_long", long)
                .with("id_chat_state", chatStateId)
        } else {
            r.hashMap("creation_time", r.now())
                .with("id_user", userId)
                .with("id_subcategory", subcategoryId)
                .with("lat", lat)
                .with("long", long)
                .with("id_chat_state", chatStateId)
        }


        val itemInserted = table.insert(
            itemToInsert
        ).run<HashMap<*, *>>(con)

        return try {
            (itemInserted["generated_keys"] as ArrayList<String>)[0] //Id inserted
        } catch (e: Exception) {
            null
        }
    }

    fun getChatsByUser(con: Connection, idUser: String): ArrayList<HashMap<*, *>> {
        val cursor =
            table.orderBy().optArg("index", r.desc("creation_time"))
                .filter(r.hashMap("id_user", idUser))
                .run(con) as Cursor<HashMap<*, *>>
        return cursor.bufferedItems()
    }

    fun getChatById(con: Connection, idChat: String): HashMap<*, *>? {
        return table.get(idChat).run(con) as HashMap<*, *>?
    }
}


