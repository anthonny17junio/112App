@file:Suppress("UNCHECKED_CAST")

package com.teltronic.app112.database.rethink.tb_messages

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import com.rethinkdb.net.Cursor
import com.teltronic.app112.classes.enums.NamesRethinkdb
import com.teltronic.app112.database.rethink.tb_chats.ChatsRethink
import java.lang.Exception

object MessagesRethink {
    private val r = RethinkDB.r
    private val table = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)

    fun sendTextMessage(
        con: Connection,
        message: String,
        idUser: String,
        idChat: String,
        idMessageType: Int
    ): String? {
        val chat = ChatsRethink.getChatById(con, idChat)
        if (chat != null) {
            val messageToInsert = r.hashMap("creation_time", r.now())
                .with("id_user", idUser)
                .with("id_chat", idChat)
                .with("id_type", idMessageType)
                .with("content", message)

            val itemInserted = table.insert(
                messageToInsert
            ).run<HashMap<*, *>>(con)

            return try {
                val generatedKeys = itemInserted["generated_keys"] as ArrayList<String>
                generatedKeys[0] //Id inserted
            } catch (e: Exception) {
                null
            }
        } else {
            return null
        }
    }

    fun getMessages(con: Connection, idChat: String): ArrayList<java.util.HashMap<*, *>> {
        val cursor =
            table.orderBy().optArg("index", r.asc("creation_time"))
                .filter(r.hashMap("id_chat", idChat))
                .run(con) as Cursor<java.util.HashMap<*, *>>
        return cursor.bufferedItems()
    }
}