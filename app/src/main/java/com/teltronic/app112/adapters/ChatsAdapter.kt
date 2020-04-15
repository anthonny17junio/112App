package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.room.chats.ChatEntity
import com.teltronic.app112.databinding.ItemChatBinding
import java.text.SimpleDateFormat
import java.util.*

//  class ChatsAdapter(private val context: Context) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
class ChatsAdapter(private val context: Context) :
    ListAdapter<ChatEntity, ChatsAdapter.ViewHolder>(ChatsDiffCallback()) {

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(this, parent)
    }

    class ViewHolder private constructor(
        val binding: ItemChatBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(chatsAdapter: ChatsAdapter, parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, chatsAdapter.context)
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

            binding.tvCategory.text = strCategory
            binding.tvSubcategory.text = strSubcategory
            binding.tvLocation.text = chat.location
            binding.tvDate.text = sdfDate.format(creationDate)
            binding.tvHour.text = sdfHour.format(creationDate)

            chatState?.let {
                binding.ivChatState.setImageResource(chatState.idIcon)
                binding.tvChatState.text = context.getText(chatState.idTitle)
                binding.ivChatState.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        chatState.idColor
                    )
                )
                binding.tvChatState.setTextColor(
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
