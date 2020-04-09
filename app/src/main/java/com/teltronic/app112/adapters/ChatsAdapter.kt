package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.room.chats.ChatEntity

class ChatsAdapter(private val context: Context) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    var data = listOf<ChatEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = data[position]

        val subcategory = Subcategory.getById(chat.id_subcategory)
        val category = subcategory?.category

        val chatState = ChatState.getById(chat.id_chat_state)

        var strSubcategory: CharSequence = ""
        subcategory?.let { strSubcategory = context.getText(subcategory.idTitle) }

        var strCategory: CharSequence = ""
        category?.let { strCategory = context.getText(category.idTitle) }

        val sdfDate = java.text.SimpleDateFormat("dd-MM-yyyy")
        val sdfHour = java.text.SimpleDateFormat("HH:mm")
        val creationDate = java.util.Date(chat.creation_epoch_time.toLong() * 1000)

        holder.tvCategory.text = strCategory
        holder.tvSubcategory.text = strSubcategory
        holder.tvLocation.text = chat.location
        holder.tvDate.text = sdfDate.format(creationDate)
        holder.tvHour.text = sdfHour.format(creationDate)

        chatState?.let {
            holder.ivChatState.setImageResource(chatState.idIcon)
            holder.tvChatState.text = context.getText(chatState.idTitle)
            holder.ivChatState.setColorFilter(
                ContextCompat.getColor(
                    context,
                    chatState.idColor
                )
            )
            holder.tvChatState.setTextColor(
                ContextCompat.getColor(
                    context,
                    chatState.idColor
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivChatState: ImageView = itemView.findViewById(R.id.ivChatState)
        val tvChatState: TextView = itemView.findViewById(R.id.tvChatState)
        val tvSubcategory: TextView = itemView.findViewById(R.id.tvSubcategory)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
    }
}

