package com.teltronic.app112.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.database.room.chats.ChatEntity
import com.teltronic.app112.databinding.ItemChatBinding

//  class ChatsAdapter(private val context: Context) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
class ChatsAdapter :
    ListAdapter<ChatEntity, ChatsAdapter.ViewHolder>(ChatsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        val binding: ItemChatBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            chat: ChatEntity
        ) {
            binding.chat = chat
            binding.executePendingBindings()
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
