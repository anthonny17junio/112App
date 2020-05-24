package com.teltronic.app112.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.classes.enums.MessageType
import com.teltronic.app112.database.room.messages.MessageEntity
import com.teltronic.app112.databinding.ItemMessageImageMeBinding
import com.teltronic.app112.databinding.ItemMessageImageOtherBinding
import com.teltronic.app112.databinding.ItemMessageTextMeBinding
import com.teltronic.app112.databinding.ItemMessageTextOtherBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_TEXT_ME = 0
private const val ITEM_VIEW_TYPE_TEXT_OTHER = 1
private const val ITEM_VIEW_TYPE_IMAGE_ME = 2
private const val ITEM_VIEW_TYPE_IMAGE_OTHER = 3

@Suppress("UNREACHABLE_CODE")
class MessagesAdapter(private val idUserRoom: String, val clickListener: MessageListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(MessagesDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return return when (viewType) {
            ITEM_VIEW_TYPE_TEXT_ME ->
                ViewHolderTextMe.from(parent)
            ITEM_VIEW_TYPE_TEXT_OTHER ->
                ViewHolderTextOther.from(parent)
            ITEM_VIEW_TYPE_IMAGE_ME ->
                ViewHolderImageMe.from(parent)
            ITEM_VIEW_TYPE_IMAGE_OTHER ->
                ViewHolderImageOther.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderTextMe -> {
                val messageItem = getItem(position) as DataItem.TextMessage
                holder.bind(messageItem.messageEntity)
            }
            is ViewHolderTextOther -> {
                val messageItem = getItem(position) as DataItem.TextMessage
                holder.bind(messageItem.messageEntity)
            }
            is ViewHolderImageOther -> {
                val messageItem = getItem(position) as DataItem.TextMessage
                holder.bind(messageItem.messageEntity, clickListener)
            }
            is ViewHolderImageMe -> {
                val messageItem = getItem(position) as DataItem.TextMessage
                holder.bind(messageItem.messageEntity, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        val idUserMessage = message.idUser
        val idType = message.idMessageType
        return when (message) {
            is DataItem.TextMessage ->
                if (idUserRoom == idUserMessage) {
                    if (idType == MessageType.IMAGE.id)
                        ITEM_VIEW_TYPE_IMAGE_ME
                    else
                        ITEM_VIEW_TYPE_TEXT_ME
                } else {
                    if (idType == MessageType.IMAGE.id)
                        ITEM_VIEW_TYPE_IMAGE_OTHER
                    else
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
        fun bind(
            item: MessageEntity
        ) {
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

    class ViewHolderImageOther private constructor(val binding: ItemMessageImageOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: MessageEntity,
            clickListener: MessageListener
        ) {
            binding.message = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderImageOther {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageImageOtherBinding.inflate(layoutInflater, parent, false)
                return ViewHolderImageOther(binding)
            }
        }
    }

    class ViewHolderImageMe private constructor(val binding: ItemMessageImageMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: MessageEntity,
            clickListener: MessageListener
        ) {
            binding.message = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderImageMe {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageImageMeBinding.inflate(layoutInflater, parent, false)
                return ViewHolderImageMe(binding)
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

        override val idMessageType: Int
            get() = messageEntity.id_message_type
    }

    abstract val id: String
    abstract val idUser: String
    abstract val idMessageType: Int
}

class MessageListener(val clickListener: (idMessage: String) -> Unit) {
    fun onClick(message: MessageEntity) = clickListener(message.id)
}