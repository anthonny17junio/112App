package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.room.chats.ChatEntity
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("category")
fun TextView.setCategory(chat: ChatEntity) {
    chat.let {
        val subcategory = Subcategory.getById(chat.id_subcategory)
        val category = subcategory?.category
        var strCategory: CharSequence = ""
        category?.let {
            strCategory = context.getText(category.idTitle)
        }
        text = strCategory
    }
}

@BindingAdapter("subcategory")
fun TextView.setSubcategory(chat: ChatEntity) {
    chat.let {
        val subcategory = Subcategory.getById(chat.id_subcategory)
        var strSubcategory: CharSequence = ""
        subcategory?.let {
            strSubcategory = context.getText(subcategory.idTitle)
        }
        text = strSubcategory
    }
}

@BindingAdapter("location")
fun TextView.setLocation(chat: ChatEntity) {
    chat.let {
        text = chat.location
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("date")
fun TextView.setDate(chat: ChatEntity) {
    chat.let {
        val sdfDate = SimpleDateFormat("dd-MM-yyyy")
        val creationDate = Date(chat.creation_epoch_time.toLong() * 1000)
        text = sdfDate.format(creationDate)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("time")
fun TextView.setTime(chat: ChatEntity) {
    chat.let {
        val sdfTime = SimpleDateFormat("HH:mm")
        val creationDate = Date(chat.creation_epoch_time.toLong() * 1000)
        text = sdfTime.format(creationDate)
    }
}

@BindingAdapter("chatStateText")
fun TextView.setChatState(chat: ChatEntity) {
    chat.let {
        val chatState = ChatState.getById(chat.id_chat_state)
        chatState?.let {
            text = context.getText(chatState.idTitle)
            setTextColor(
                ContextCompat.getColor(
                    context,
                    chatState.idColor
                )
            )
        }
    }
}

@BindingAdapter("chatStateImage")
fun ImageView.setChatState(chat: ChatEntity) {
    chat.let {
        val chatState = ChatState.getById(chat.id_chat_state)
        chatState?.let {
            setImageResource(chatState.idIcon)
            setColorFilter(
                ContextCompat.getColor(
                    context,
                    chatState.idColor
                )
            )
        }
    }
}

class ChatListener(val clickListener: (chatId: String) -> Unit) {
    fun onClick(chat: ChatEntity) = clickListener(chat.id)
}