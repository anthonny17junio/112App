package com.teltronic.app112.database.room.notices

import org.json.simple.JSONObject


object NoticeEntityConverter {
    fun fromHashMap(hshMap: HashMap<String, *>?): NoticeEntity? {
        if (hshMap != null) {
            val id = hshMap["id"] as String
            val creationTime = hshMap["creation_time"] as JSONObject
            val creationTimezone = creationTime["timezone"] as String
            val creationEpochTime = try {
                creationTime["epoch_time"] as Double
            } catch (e: ClassCastException) {
                (creationTime["epoch_time"] as Long).toDouble()
            }
            val contentTitle = hshMap["content_title"] as String
            val contentText = hshMap["content_text"] as String
            val contentImage = hshMap["content_image"] as String?

            return NoticeEntity(
                id,
                contentTitle,
                creationEpochTime,
                creationTimezone,
                contentText,
                contentImage,
                false
            )

        } else {
            return null
        }
    }
}