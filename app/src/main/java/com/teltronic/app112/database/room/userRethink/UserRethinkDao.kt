package com.teltronic.app112.database.room.userRethink

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserRethinkDao {
    @Insert
    fun insert(userRethink: UserRethinkEntity)

    @Update
    fun update(userRethink: UserRethinkEntity)

    @Query("SELECT * FROM tb_user_rethink LIMIT 1")
    fun get(): UserRethinkEntity

    @Delete
    fun delete(userRethink: UserRethinkEntity)

    @Query("DELETE FROM tb_user_rethink")
    fun deleteAll()

    @Query("SELECT * FROM tb_user_rethink LIMIT 1")
    fun getLiveData(): LiveData<UserRethinkEntity>
}