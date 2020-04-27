package com.teltronic.app112.database.room.messages

import android.content.Context
import org.json.simple.JSONObject

object MessageEntityConverter {

    fun fromHashMap(hshMap: HashMap<String, *>?, context: Context): MessageEntity? {
        if (hshMap != null) {
            val id = hshMap["id"] as String
            val idUser = hshMap["id_user"] as String
            val idChat = hshMap["id_chat"] as String
            val creationTime = hshMap["creation_time"] as JSONObject
            val creationTimezone = creationTime["timezone"] as String
            val creationEpochTime = creationTime["epoch_time"] as Double
            val idMessageType = hshMap["id_type"] as Long
            val content = hshMap["content"] as String
            return MessageEntity(
                id,
                idUser,
                idChat,
                creationEpochTime,
                creationTimezone,
                idMessageType.toInt(),
                content
            )
        } else {
            return null
        }
    }
}