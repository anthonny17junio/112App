@file:Suppress("SENSELESS_COMPARISON")

package com.teltronic.app112.database.room.chats

import android.content.Context
import com.teltronic.app112.classes.Phone
import org.json.simple.JSONObject
import java.lang.ClassCastException

object ChatEntityConverter {
    fun fromHashMap(hshMap: HashMap<String, *>?, context: Context): ChatEntity? {
        if (hshMap != null) {
            val id = hshMap["id"] as String
            val idSubcategory = hshMap["id_subcategory"] as Long
            val idChatState = hshMap["id_chat_state"] as Long
            val creationTime = hshMap["creation_time"] as JSONObject
            val creationTimezone = creationTime["timezone"] as String
            val creationEpochTime = try {
                creationTime["epoch_time"] as Double
            } catch (e: ClassCastException) {
                (creationTime["epoch_time"] as Long).toDouble()
            }
            val lat = try {
                hshMap["lat"] as Double
            } catch (e: ClassCastException) {
                (hshMap["lat"] as Long).toDouble()
            }
            val long =  try {
                hshMap["long"] as Double
            } catch (e: ClassCastException) {
                (hshMap["long"] as Long).toDouble()
            }

            val lastLanObj = try {
                hshMap["last_lat"] as Double
            } catch (e: ClassCastException) {
                (hshMap["last_lat"] as Long).toDouble()
            }
            val lastLongObj= try {
                hshMap["last_long"] as Double
            } catch (e: ClassCastException) {
                (hshMap["last_long"] as Long).toDouble()
            }
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