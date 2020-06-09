package com.teltronic.app112.database.room.userRethink

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_user_rethink")
data class UserRethinkEntity(
    @PrimaryKey
    val id: Int = 0,
    var id_rethink: String = ""
)