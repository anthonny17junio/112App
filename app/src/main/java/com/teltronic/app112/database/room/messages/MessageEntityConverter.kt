package com.teltronic.app112.database.room.messages

import android.util.Base64
import com.teltronic.app112.classes.enums.MessageType
import org.json.simple.JSONObject
import java.lang.ClassCastException

object MessageEntityConverter {

    fun fromHashMap(hshMap: HashMap<String, *>?): MessageEntity? {
        if (hshMap != null) {
            val id = hshMap["id"] as String
            val idUser = hshMap["id_user"] as String
            val idChat = hshMap["id_chat"] as String
            val creationTime = hshMap["creation_time"] as JSONObject
            val creationTimezone = creationTime["timezone"] as String
            val creationEpochTime = try {
                creationTime["epoch_time"] as Double
            } catch (e: ClassCastException) {
                (creationTime["epoch_time"] as Long).toDouble()
            }

            val idMessageType = hshMap["id_type"] as Long

            return when (idMessageType.toInt()) {
                MessageType.TEXT.id -> {
                    val content = hshMap["content"] as String
                    MessageEntity(
                        id,
                        idUser,
                        idChat,
                        creationEpochTime,
                        creationTimezone,
                        idMessageType.toInt(),
                        content
                    )
                }

                MessageType.IMAGE.id -> {
                    val strImage = try {
                        Base64.encodeToString((hshMap["content"] as ByteArray), Base64.DEFAULT)
                    } catch (e: ClassCastException) {
                        (hshMap["content"] as JSONObject)["data"] as String //Cuando se restauran los mensajes
                    }
                    MessageEntity(
                        id,
                        idUser,
                        idChat,
                        creationEpochTime,
                        creationTimezone,
                        idMessageType.toInt(),
                        strImage
                    )
                }

                else -> null
            }

        } else {
            return null
        }
    }
}