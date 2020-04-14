package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.room.chats.ChatEntity
import java.text.SimpleDateFormat
import java.util.*

//  class ChatsAdapter(private val context: Context) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    class ChatsAdapter(private val context: Context) : ListAdapter<ChatEntity, ChatsAdapter.ViewHolder>(ChatsDiffCallback()) {

//    var data = listOf<ChatEntity>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

//    override fun getItemCount() = data.size


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(this, parent)
    }

    class ViewHolder private constructor(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        private val ivChatState: ImageView = itemView.findViewById(R.id.ivChatState)
        private val tvChatState: TextView = itemView.findViewById(R.id.tvChatState)
        private val tvSubcategory: TextView = itemView.findViewById(R.id.tvSubcategory)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvHour: TextView = itemView.findViewById(R.id.tvHour)

        companion object {
            fun from(chatsAdapter: ChatsAdapter, parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_chat, parent, false)
                return ViewHolder(view, chatsAdapter.context)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun bind(
            chat: ChatEntity
        ) {
            val subcategory = Subcategory.getById(chat.id_subcategory)
            val category = subcategory?.category

            val chatState = ChatState.getById(chat.id_chat_state)

            var strSubcategory: CharSequence = ""
            subcategory?.let { strSubcategory = context.getText(subcategory.idTitle) }

            var strCategory: CharSequence = ""
            category?.let { strCategory = context.getText(category.idTitle) }

            val sdfDate = SimpleDateFormat("dd-MM-yyyy")
            val sdfHour = SimpleDateFormat("HH:mm")
            val creationDate = Date(chat.creation_epoch_time.toLong() * 1000)

            tvCategory.text = strCategory
            tvSubcategory.text = strSubcategory
            tvLocation.text = chat.location
            tvDate.text = sdfDate.format(creationDate)
            tvHour.text = sdfHour.format(creationDate)

            chatState?.let {
                ivChatState.setImageResource(chatState.idIcon)
                tvChatState.text = context.getText(chatState.idTitle)
                ivChatState.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        chatState.idColor
                    )
                )
                tvChatState.setTextColor(
                    ContextCompat.getColor(
                        context,
                        chatState.idColor
                    )
                )
            }
        }
    }
}

class ChatsDiffCallback : DiffUtil.ItemCallback<ChatEntity>() {
    override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
        return oldItem == newItem
    }

}
