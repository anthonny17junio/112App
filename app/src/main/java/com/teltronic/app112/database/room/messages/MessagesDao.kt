package com.teltronic.app112.database.room.messages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MessagesDao {
    @Insert
    fun insert(message: MessageEntity)

    @Query("SELECT * FROM tb_messages WHERE id= :idMessage")
    fun get(idMessage: String): MessageEntity?

    @Query("DELETE FROM tb_messages WHERE id= :idMessage")
    fun delete(idMessage: String)

    @Query("DELETE FROM tb_messages")
    fun deleteAll()

    @Query("DELETE FROM tb_messages WHERE id_chat= :idChat")
    fun deleteAllByChat(idChat: String)

    @Update
    fun update(chat: MessageEntity)
}