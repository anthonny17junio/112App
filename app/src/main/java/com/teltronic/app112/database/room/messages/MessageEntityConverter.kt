package com.teltronic.app112.database.room.messages

import com.teltronic.app112.classes.enums.MessageType
import org.json.simple.JSONObject
import java.util.*

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
                    val content = hshMap["content"] as String
//                    content = try {
//                        //Si solo llega la url
//                        hshMap["content"] as String
//                    } catch (e: ClassCastException) {
//                        //Si llega la imagen
//                        val image64 = (hshMap["content"] as JSONObject)["data"] as String
//                        val imageBytes = Base64.decode(image64, Base64.DEFAULT)
//                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//                        //Guardar image
//                        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                        val wrapper = ContextWrapper(applicationContext)
//                        "url"
//                    }
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

                else -> null
            }

        } else {
            return null
        }
    }
}