package com.teltronic.app112.database.room.chats

import android.content.Context
import com.teltronic.app112.classes.Phone
import org.json.simple.JSONObject

object ChatEntityConverter {
    fun fromHashMap(hshMap: HashMap<String, *>?,context: Context): ChatEntity? {
        if (hshMap != null) {
            val id = hshMap["id"] as String
            val idSubcategory = hshMap["id_subcategory"] as Long
            val idChatState = hshMap["id_chat_state"] as Long
            val creationTime = hshMap["creation_time"] as JSONObject
            val creationTimezone = creationTime["timezone"] as String
            val creationEpochTime = creationTime["epoch_time"] as Double
            val lat = hshMap["lat"] as Double
            val long = hshMap["long"] as Double

            val lastLanObj = creationTime["last_lat"]
            val lastLongObj = creationTime["last_long"]
            val realTime = !(lastLanObj == null || lastLongObj == null)

            val cityName = Phone.getCityName(context, lat, long)

            return ChatEntity(
                id,
                idSubcategory.toInt(),
                idChatState.toInt(),
                creationEpochTime,
                creationTimezone,
                cityName,
                realTime
            )
        } else {
            return null
        }
    }
}