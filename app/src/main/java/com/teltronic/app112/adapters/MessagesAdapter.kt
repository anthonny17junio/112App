package com.teltronic.app112.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.R
import com.teltronic.app112.database.room.messages.MessageEntity

class MessagesAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<MessageEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.content
    }

}

class TextItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
