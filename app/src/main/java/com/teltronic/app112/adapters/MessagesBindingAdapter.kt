package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teltronic.app112.database.room.messages.MessageEntity
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("messageText")
fun TextView.setMessageText(message:MessageEntity?){
    message?.let{
        text = message.content
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("messageTime")
fun TextView.setTime(message: MessageEntity?){
    message?.let{
        val sdfHour = SimpleDateFormat("HH:mm")
        val creationDate = Date(message.creation_epoch_time.toLong() * 1000)
        text = sdfHour.format(creationDate)
    }
}
