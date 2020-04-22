package com.teltronic.app112.database.rethink.tb_messages

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import com.teltronic.app112.classes.enums.NamesRethinkdb
import com.teltronic.app112.database.rethink.tb_chats.ChatsRethink

object MessagesRethink {
    private val r = RethinkDB.r
    private val table = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
    //indices = "creation_time", "id_user", "id_chat", "id_chat"

    fun sendTextMessage(con: Connection, message: String, idUser: String, idChat: String): String? {
        val chat = ChatsRethink.getChatById(con, idChat)
        if (chat != null) {
            //Insertar mensaje
            return "idMessageInserted"
        } else {
            return null
        }
    }
}