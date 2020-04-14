package com.teltronic.app112.database.room.chats

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_chats")
data class ChatEntity(
    @PrimaryKey
    var id: String,
    var id_subcategory: Int,
    var id_chat_state: Int,
    var creation_epoch_time: Double,
    var creation_timezone: String,
    var location: String,
    var real_time:Boolean
)
