@file:Suppress("UNCHECKED_CAST")

package com.teltronic.app112.database.rethink.tb_messages

import android.util.Base64
import com.rethinkdb.RethinkDB
import com.rethinkdb.model.OptArgs
import com.rethinkdb.net.Connection
import com.teltronic.app112.classes.enums.NamesRethinkdb
import com.teltronic.app112.database.rethink.tb_chats.ChatsRethink
import java.lang.Exception
import java.net.URLEncoder

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

    fun sendImageMessage(
        con: Connection,
        imageByteArray: ByteArray,
        idUser: String,
        idChat: String,
        idMessageType: Int
    ): String? {
        var strImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT) //Para solucionar problema simulador web encodign
        val chat = ChatsRethink.getChatById(con, idChat)
        if (chat != null) {
            val messageToInsert = r.hashMap("creation_time", r.now())
                .with("id_user", idUser)
                .with("id_chat", idChat)
                .with("id_type", idMessageType)
//                .with("content", r.binary(imageByteArray))
                .with("content", strImage) //Para solucionar problema simulador web encodign

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


    fun getMessagesIds(con: Connection, idChat: String): ArrayList<String> {
        return r.db("db112").table("tb_messages").getAll(idChat).optArg("index", "id_chat")
            .orderBy("creation_time").getField("id")
            .run(con) as ArrayList<String>
    }

    fun getMessage(con: Connection, idChat: String): HashMap<*, *>? {
        return table.get(idChat).run(con, OptArgs.of("time_format", "raw")) as HashMap<*, *>?
    }

    fun getMessageWithoutFile(con: Connection, idChat: String): HashMap<*, *>? {
        return table.get(idChat).without("content")
            .run(con, OptArgs.of("time_format", "raw")) as HashMap<*, *>?
    }
}