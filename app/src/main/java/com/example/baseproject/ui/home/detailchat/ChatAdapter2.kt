package com.example.baseproject.ui.home.detailchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
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

interface OnPhotoClickListener {
    fun onPhotoClick(chat: ChatModel)
}

class ChatAdapter2(private val onPhotoClickListener: OnPhotoClickListener) : ListAdapter<ChatModel, RecyclerView.ViewHolder>(
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
                if (item.formatDate != tvDate.context.getString(R.string.same_minute)) {
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

                if (item.formatDate != tvDate.context.getString(R.string.same_minute)) {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }

            binding.root.setOnClickListener {
                onPhotoClickListener.onPhotoClick(getItem(adapterPosition))
            }
        }
    }

    inner class ItemChatSendVH(private val binding: ItemChatSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.apply {
                tvItemSendMess.background = ContextCompat.getDrawable(
                    root.context,
                    when (item.typeLayout) {
                        TypeLayoutChat.BETWEEN -> R.drawable.bg_chat_bottom_top
                        TypeLayoutChat.END -> R.drawable.bg_chat_top_right
                        TypeLayoutChat.ONE -> R.drawable.bg_tv_send
                        TypeLayoutChat.START -> R.drawable.bg_chat_bottom_right
                        else -> R.drawable.bg_chat_bottom_right
                    }
                )
                tvItemSendMess.text = item.text
                if (item.formatDate != tvDate.context.getString(R.string.same_minute)) {
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

                if (item.formatDate != tvDate.context.getString(R.string.same_minute)) {
                    tvDate.text = item.formatDate
                    tvDate.visible()
                } else tvDate.gone()
            }

            binding.root.setOnClickListener {
                onPhotoClickListener.onPhotoClick(getItem(adapterPosition))
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

                if (item.formatDate != tvDate.context.getString(R.string.same_minute)) {
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

                if (item.formatDate != tvDate.context.getString(R.string.same_minute)) {
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
            Chat.RECEIVE_TEXT -> {
                val binding =
                    ItemChatReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatReceiveVH(binding)
            }

            Chat.SEND_TEXT -> {
                val binding =
                    ItemChatSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatSendVH(binding)
            }

            Chat.RECEIVE_PHOTOS -> {
                val binding =
                    ItemPhotoReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOnePhotoReceiveVH(binding)
            }

            Chat.SEND_EMOJI -> {
                val binding =
                    ItemEmojiSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOneEmojiSendVH(binding)
            }

            Chat.RECEIVE_EMOJI -> {
                val binding =
                    ItemEmojiReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOneEmojiReceiveVH(binding)
            }

            Chat.DATE -> {
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
            Chat.RECEIVE_TEXT -> {
                val mHolder = holder as ItemChatReceiveVH
                mHolder.bind(chat)
            }

            Chat.SEND_TEXT -> {
                val mHolder = holder as ItemChatSendVH
                mHolder.bind(chat)
            }

            Chat.RECEIVE_PHOTOS -> {
                val mHolder = holder as ItemChatOnePhotoReceiveVH
                mHolder.bind(chat)
            }

            Chat.SEND_PHOTOS -> {
                val mHolder = holder as ItemChatOnePhotoSendVH
                mHolder.bind(chat)
            }

            Chat.RECEIVE_EMOJI -> {
                val mHolder = holder as ItemChatOneEmojiReceiveVH
                mHolder.bind(chat)
            }

            Chat.SEND_EMOJI -> {
                val mHolder = holder as ItemChatOneEmojiSendVH
                mHolder.bind(chat)
            }

            Chat.DATE -> {
                val mHolder = holder as ItemChatDateVH
                mHolder.bind(chat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = getItem(position)
        if (chat.type == MessageType.DATE) return Chat.DATE
        else if (chat.idSender == user?.uid) {
            return when (chat.type) {
                MessageType.PHOTO -> Chat.SEND_PHOTOS

                MessageType.TEXT -> Chat.SEND_TEXT

                else -> Chat.SEND_EMOJI

            }
        } else {
            return when (chat.type) {
                MessageType.TEXT -> Chat.RECEIVE_TEXT

                MessageType.PHOTO -> Chat.RECEIVE_PHOTOS

                else -> Chat.RECEIVE_EMOJI
            }
        }
    }
}