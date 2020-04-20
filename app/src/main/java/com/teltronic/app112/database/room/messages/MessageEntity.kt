package com.teltronic.app112.database.room.messages

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tb_messages", indices = arrayOf(Index(value = ["id_chat"])))
data class MessageEntity(
    @PrimaryKey
    var id: String,
    var id_user: String,
    var id_chat: String,
    var creation_epoch_time: Double,
    var creation_timezone: String,
    var id_message_type: Int,
    var content: String
)