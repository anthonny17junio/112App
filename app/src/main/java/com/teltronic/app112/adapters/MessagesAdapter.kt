package com.teltronic.app112.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.database.room.messages.MessageEntity
import com.teltronic.app112.databinding.ItemMessageTextMeBinding
import com.teltronic.app112.databinding.ItemMessageTextOtherBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_TEXT_ME = 0
private const val ITEM_VIEW_TYPE_TEXT_OTHER = 1

@Suppress("UNREACHABLE_CODE")
class MessagesAdapter(private val idUserRoom: String) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(MessagesDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return return when (viewType) {
            ITEM_VIEW_TYPE_TEXT_ME ->
                ViewHolderTextMe.from(parent)
            ITEM_VIEW_TYPE_TEXT_OTHER ->
                ViewHolderTextOther.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderTextMe -> {
                val messageItem = getItem(position) as DataItem.TextMessage
                holder.bind(messageItem.messageEntity)
            }
            is ViewHolderTextOther ->{
                val messageItem = getItem(position) as DataItem.TextMessage
                holder.bind(messageItem.messageEntity)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        val idUserMessage = message.idUser
        return when (message) {
            is DataItem.TextMessage ->
                if (idUserRoom == idUserMessage) {
                    ITEM_VIEW_TYPE_TEXT_ME
                } else {
                    ITEM_VIEW_TYPE_TEXT_OTHER
                }
        }
    }

    fun submitMessagesList(list: List<MessageEntity>) {
        adapterScope.launch {
            val items = list.map { DataItem.TextMessage(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class ViewHolderTextMe private constructor(val binding: ItemMessageTextMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageEntity) {
            binding.message = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderTextMe {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageTextMeBinding.inflate(layoutInflater, parent, false)
                return ViewHolderTextMe(binding)
            }
        }
    }

    class ViewHolderTextOther private constructor(val binding: ItemMessageTextOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageEntity) {
            binding.message = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderTextOther {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageTextOtherBinding.inflate(layoutInflater, parent, false)
                return ViewHolderTextOther(binding)
            }
        }
    }
}

class MessagesDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

sealed class DataItem {
    data class TextMessage(val messageEntity: MessageEntity) : DataItem() {
        override val id: String
            get() = messageEntity.id

        override val idUser: String
            get() = messageEntity.id_user
    }

    abstract val id: String
    abstract val idUser: String
}