package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.teltronic.app112.R
import com.teltronic.app112.database.room.notices.NoticeEntity
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("title")
fun TextView.setTitle(notice: NoticeEntity) {
    notice.let {
        text = notice.title
    }
}

@BindingAdapter("message")
fun TextView.setMessage(notice: NoticeEntity) {
    notice.let {
        text = notice.message
    }
}


@BindingAdapter("noticeState")
fun ImageView.setNoticeState(notice: NoticeEntity) {
    notice.let {
        var imageNoticeState = R.drawable.ic_notice_no_read
        if (notice.read) {
            imageNoticeState = R.drawable.ic_notice_read
        }

        setImageResource(imageNoticeState)
    }
}

@BindingAdapter("location")
fun TextView.setLocation(notice: NoticeEntity) {
    notice.let {
        text = notice.location
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("date")
fun TextView.setDate(notice: NoticeEntity) {
    notice.let {
        val sdfDate = SimpleDateFormat("dd-MM-yyyy")
        val creationDate = Date(notice.creation_epoch_time.toLong() * 1000)
        text = sdfDate.format(creationDate)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("time")
fun TextView.setTime(notice: NoticeEntity) {
    notice.let {
        val sdfTime = SimpleDateFormat("HH:mm")
        val creationDate = Date(notice.creation_epoch_time.toLong() * 1000)
        text = sdfTime.format(creationDate)
    }
}


@BindingAdapter("backgroundState")
fun ConstraintLayout.setBackground(notice: NoticeEntity) {
    notice.let {
        var backgroundColor = R.color.colorWhite
        if(!notice.read){
            backgroundColor = R.color.colorBackgroundChat
        }
        setBackgroundColor(
            ContextCompat.getColor(
                context, backgroundColor
            )
        )
    }
}

class NoticeListener(val clickListener: (chatId: String) -> Unit) {
    fun onClick(notice: NoticeEntity) = clickListener(notice.id)
}