package com.teltronic.app112.database.room.notices

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_notices")
data class NoticeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val creation_epoch_time: Double,
    val creation_timezone: String,
    val message: String,
    val urlPhoto: String?,
    val read: Boolean
)