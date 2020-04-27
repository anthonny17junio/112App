package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.R
import com.teltronic.app112.database.room.messages.MessageEntity
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    var data = listOf<MessageEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_message_text_other, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        val sdfHour = SimpleDateFormat("HH:mm")
        val creationDate = Date(item.creation_epoch_time.toLong() * 1000)

        holder.tvMessage.text = item.content
        holder.tvHour.text = sdfHour.format(creationDate)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
    }
}

