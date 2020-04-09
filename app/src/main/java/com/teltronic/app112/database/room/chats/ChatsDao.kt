package com.teltronic.app112.database.room.chats

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChatsDao {
    @Insert
    fun insert(configurations: ChatEntity)

    @Update
    fun update(configurations: ChatEntity)

    @Query("SELECT * FROM tb_chats ORDER BY creation_epoch_time DESC")
    fun getAll(): LiveData<List<ChatEntity>>

    @Query("SELECT * FROM tb_chats WHERE id= :idChat")
    fun get(idChat: String): ChatEntity?

    @Query("DELETE FROM tb_chats")
    fun deleteAll()
}