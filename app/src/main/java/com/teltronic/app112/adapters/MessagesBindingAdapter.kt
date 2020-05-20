package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teltronic.app112.database.room.messages.MessageEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@BindingAdapter("messageText")
fun TextView.setMessageText(message: MessageEntity?) {
    message?.let {
        text = message.content
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("messageTime")
fun TextView.setTime(message: MessageEntity?) {
    message?.let {
        val sdfHour = SimpleDateFormat("HH:mm")
        val creationDate = Date(message.creation_epoch_time.toLong() * 1000)
        text = sdfHour.format(creationDate)
    }
}


@BindingAdapter("messageImage")
fun ImageView.setImage(message: MessageEntity?) {
    message?.let {
        val urlImage = message.content

        var imageBitmap = BitmapFactory.decodeFile(urlImage)
        if (imageBitmap != null)
            imageBitmap = imageBitmap.resizeByWidth(300)
        setImageBitmap(imageBitmap)
    }
}

fun Bitmap.resizeByWidth(width: Int): Bitmap {
    val ratio: Float = this.width.toFloat() / this.height.toFloat()
    val height: Int = (width / ratio).roundToInt()

    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        false
    )
}
