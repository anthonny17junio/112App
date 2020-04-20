package com.teltronic.app112.database.room.chats

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teltronic.app112.database.room.chatWithMessages.ChatWithMessages

@Dao
interface ChatsDao {
    @Insert
    fun insert(chat: ChatEntity)

    @Update
    fun update(chat: ChatEntity)

    @Query("SELECT * FROM tb_chats ORDER BY creation_epoch_time DESC")
    fun getAll(): LiveData<List<ChatEntity>>

    @Query("SELECT * FROM tb_chats WHERE id= :idChat")
    fun get(idChat: String): ChatEntity?

    @Query("DELETE FROM tb_chats") //todo comprobar que al eliminar el chat se eliminan los mensajes
    fun deleteAll()

    @Query("DELETE FROM tb_chats WHERE id= :idChat") //todo comprobar que al eliminar el chat se eliminan los mensajes
    fun delete(idChat: String)

    @Transaction
    @Query("SELECT * FROM tb_chats WHERE id= :idChat")
    fun getChatWithMessages(idChat: String): LiveData<ChatWithMessages>
}