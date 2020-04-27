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
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = data.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item)
    }


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)

        fun bind(item: MessageEntity) {
            val sdfHour = SimpleDateFormat("HH:mm")
            val creationDate = Date(item.creation_epoch_time.toLong() * 1000)

            tvMessage.text = item.content
            tvHour.text = sdfHour.format(creationDate)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_text_other, parent, false)
                return ViewHolder(view)
            }
        }
    }


}

