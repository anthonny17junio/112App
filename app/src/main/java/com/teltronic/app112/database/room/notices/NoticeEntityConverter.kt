package com.teltronic.app112.database.room.notices

import android.content.Context
import com.teltronic.app112.classes.Phone
import org.json.simple.JSONObject


object NoticeEntityConverter {
    fun fromHashMap(hshMap: HashMap<String, *>?, context: Context): NoticeEntity? {
        if (hshMap != null) {
            val id = hshMap["id"] as String
            val creationTime = hshMap["creation_time"] as JSONObject
            val creationTimezone = creationTime["timezone"] as String
            val creationEpochTime = try {
                creationTime["epoch_time"] as Double
            } catch (e: ClassCastException) {
                (creationTime["epoch_time"] as Long).toDouble()
            }
            val lat = try {
                hshMap["lat"] as Double
            } catch (e: java.lang.ClassCastException) {
                (hshMap["lat"] as Long).toDouble()
            }
            val long = try {
                hshMap["long"] as Double
            } catch (e: java.lang.ClassCastException) {
                (hshMap["long"] as Long).toDouble()
            }
            val contentTitle = hshMap["content_title"] as String
            val contentText = hshMap["content_text"] as String
            val contentImage = hshMap["content_image"] as String?
            val cityName = Phone.getCityName(context, lat, long)

            return NoticeEntity(
                id,
                contentTitle,
                creationEpochTime,
                creationTimezone,
                contentText,
                contentImage,
                cityName,
                false
            )

        } else {
            return null
        }
    }
}