package com.teltronic.app112.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.database.room.chats.ChatEntity
import com.teltronic.app112.databinding.ItemChatBinding

//  class ChatsAdapter(private val context: Context) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
class ChatsAdapter(private val clickListener: ChatListener) :
    ListAdapter<ChatEntity, ChatsAdapter.ViewHolder>(ChatsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
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
            chat: ChatEntity,
            clickListener: ChatListener
        ) {
            binding.chat = chat
            binding.clickListener = clickListener
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
