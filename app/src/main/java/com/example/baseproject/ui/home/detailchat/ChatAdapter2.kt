package com.example.baseproject.ui.home.detailchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.ItemChatDateBinding
import com.example.baseproject.databinding.ItemChatReceiveBinding
import com.example.baseproject.databinding.ItemChatSendBinding
import com.example.baseproject.databinding.ItemEmojiReceiveBinding
import com.example.baseproject.databinding.ItemEmojiSendBinding
import com.example.baseproject.databinding.ItemPhotoReceiveBinding
import com.example.baseproject.databinding.ItemPhotoSendBinding
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.extension.*
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter2 : ListAdapter<ChatModel, RecyclerView.ViewHolder>(
    ExampleListDiffUtil()
) {
    private val user = FirebaseAuth.getInstance().currentUser

    override fun submitList(list: MutableList<ChatModel>?) {
        val result = arrayListOf<ChatModel>()
        list?.forEach {
            result.add(it.copy())
        }
        super.submitList(result)
    }

    inner class ItemChatReceiveVH(private val binding: ItemChatReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                tvItemchatMess.text = item.text
                if (item.formatDate != "empty") {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }
        }
    }

    inner class ItemChatOnePhotoSendVH(private val binding: ItemPhotoSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                Glide.with(ivItemOnepicture)
                    .load(item.photo)
                    .override(
                        (300 * ivItemOnepicture.resources.displayMetrics.density).toInt(),
                        (300 * ivItemOnepicture.resources.displayMetrics.density).toInt()
                    )
                    .into(ivItemOnepicture)

                if (item.formatDate != "empty") {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }
        }
    }

    inner class ItemChatSendVH(private val binding: ItemChatSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                tvItemSendMess.text = item.text
                if (item.formatDate != "empty") {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }
        }
    }


    inner class ItemChatOnePhotoReceiveVH(private val binding: ItemPhotoReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                Glide.with(ivItemOnepicture)
                    .load(item.photo)
                    .override(
                        (300 * ivItemOnepicture.resources.displayMetrics.density).toInt(),
                        (300 * ivItemOnepicture.resources.displayMetrics.density).toInt()
                    ).into(ivItemOnepicture)

                if (item.formatDate != "empty") {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }
        }
    }

    inner class ItemChatOneEmojiReceiveVH(private val binding: ItemEmojiReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                Glide.with(ivItemOnepicture)
                    .load(item.emoji?.let { getEmoji(it.toInt()) })
                    .into(ivItemOnepicture)

                if (item.formatDate != "empty") {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }
        }
    }

    inner class ItemChatOneEmojiSendVH(private val binding: ItemEmojiSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                Glide.with(ivItemOnepicture)
                    .load(item.emoji?.let { getEmoji(it.toInt()) })
                    .into(ivItemOnepicture)

                if (item.formatDate != "empty") {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }
        }
    }

    inner class ItemChatDateVH(private val binding: ItemChatDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.tvDate.text = item.currentDate
        }
    }

    class ExampleListDiffUtil : DiffUtil.ItemCallback<ChatModel>() {
        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel) =
            oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            RECEIVE_TEXT -> {
                val binding =
                    ItemChatReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatReceiveVH(binding)
            }

            SEND_TEXT -> {
                val binding =
                    ItemChatSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatSendVH(binding)
            }

            RECEIVE_PHOTOS -> {
                val binding =
                    ItemPhotoReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOnePhotoReceiveVH(binding)
            }

            SEND_EMOJI -> {
                val binding =
                    ItemEmojiSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOneEmojiSendVH(binding)
            }

            RECEIVE_EMOJI -> {
                val binding =
                    ItemEmojiReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOneEmojiReceiveVH(binding)
            }

            DATE -> {
                val binding =
                    ItemChatDateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatDateVH(binding)
            }

            else -> {
                val binding =
                    ItemPhotoSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOnePhotoSendVH(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)
        when (holder.itemViewType) {
            RECEIVE_TEXT -> {
                val mHolder = holder as ItemChatReceiveVH
                mHolder.bind(chat)
            }

            SEND_TEXT -> {
                val mHolder = holder as ItemChatSendVH
                mHolder.bind(chat)
            }

            RECEIVE_PHOTOS -> {
                val mHolder = holder as ItemChatOnePhotoReceiveVH
                mHolder.bind(chat)
            }

            SEND_PHOTOS -> {
                val mHolder = holder as ItemChatOnePhotoSendVH
                mHolder.bind(chat)
            }

            RECEIVE_EMOJI -> {
                val mHolder = holder as ItemChatOneEmojiReceiveVH
                mHolder.bind(chat)
            }

            SEND_EMOJI -> {
                val mHolder = holder as ItemChatOneEmojiSendVH
                mHolder.bind(chat)
            }

            DATE -> {
                val mHolder = holder as ItemChatDateVH
                mHolder.bind(chat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = getItem(position)
        if (chat.type == MessageType.DATE) return DATE
        else if (chat.idSender == user?.uid) {
            return when (chat.type) {
                MessageType.PHOTO -> SEND_PHOTOS

                MessageType.TEXT -> SEND_TEXT

                else -> SEND_EMOJI

            }
        } else {
            return when (chat.type) {
                MessageType.TEXT -> RECEIVE_TEXT

                MessageType.PHOTO -> RECEIVE_PHOTOS

                else -> RECEIVE_EMOJI
            }
        }
    }
}