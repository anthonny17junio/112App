package com.teltronic.app112.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teltronic.app112.database.room.notices.NoticeEntity
import androidx.recyclerview.widget.ListAdapter
import com.teltronic.app112.databinding.ItemNoticeBinding

class NoticesAdapter(val clickListener: NoticeListener) :
    ListAdapter<NoticeEntity, NoticesAdapter.ViewHolder>(NoticeDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: NoticeEntity,
            clickListener: NoticeListener
        ) {
            binding.notice = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNoticeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class NoticeDiffCallback : DiffUtil.ItemCallback<NoticeEntity>() {
    override fun areItemsTheSame(oldItem: NoticeEntity, newItem: NoticeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoticeEntity, newItem: NoticeEntity): Boolean {
        return oldItem == newItem
    }
}