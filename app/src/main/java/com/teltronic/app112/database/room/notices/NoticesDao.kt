package com.teltronic.app112.database.room.notices

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoticesDao {
    @Insert
    fun insert(notice: NoticeEntity)

    @Query("SELECT * FROM tb_notices WHERE id= :idNotice")
    fun get(idNotice: String): NoticeEntity?

    @Query("UPDATE tb_notices SET read=:readNewValue WHERE id= :idNotice")
    fun updateRead(idNotice: String, readNewValue: Boolean)

    @Query("SELECT * FROM tb_notices ORDER BY creation_epoch_time DESC")
    fun getAll(): LiveData<List<NoticeEntity>>
}