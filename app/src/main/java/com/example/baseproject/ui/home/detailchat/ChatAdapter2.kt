package com.example.baseproject.ui.home.detailchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemChatReceiveBinding
import com.example.baseproject.databinding.ItemChatSendBinding
import com.example.baseproject.databinding.ItemEmojiReceiveBinding
import com.example.baseproject.databinding.ItemEmojiSendBinding
import com.example.baseproject.databinding.ItemPhotoReceiveBinding
import com.example.baseproject.databinding.ItemPhotoSendBinding
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.domain.model.UserModel
import com.example.baseproject.extension.*
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter2 : ListAdapter<ChatModel, RecyclerView.ViewHolder>(
    ExampleListDiffUtil()
) {
    private var receiver: UserModel? = null
    private val user = FirebaseAuth.getInstance().currentUser
    fun setReceiver(receiver: UserModel?) {
        this.receiver = receiver
    }

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
            binding.tvDate.apply {
                text = convertToMinuteSecond(getTimeCurrent())
                visibility = View.VISIBLE
            }
            binding.tvItemchatMess.text = item.text

            Glide.with(binding.ivAvatar)
                .load(receiver?.profilePicture)
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_default)
                .error(R.drawable.ic_avatar_default)
                .into(binding.ivAvatar)
        }
    }

    inner class ItemChatOnePhotoSendVH(private val binding: ItemPhotoSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            Glide.with(binding.ivItemOnepicture)
                .load(item.photo)
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)
        }
    }

    inner class ItemChatSendVH(private val binding: ItemChatSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            binding.tvItemSendMess.text = item.text
//            binding.tvItemSendMess.setOnClickListener {
//                text = convertToMinuteSecond(getTimeCurrent())
//                visibility = View.VISIBLE
//            }
        }
    }


    inner class ItemChatOnePhotoReceiveVH(private val binding: ItemPhotoReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            Glide.with(binding.ivAvatar)
                .load(receiver?.profilePicture)
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_default)
                .error(R.drawable.ic_avatar_default)
                .into(binding.ivAvatar)
            binding.tvDate.apply {
                text = convertToMinuteSecond(getTimeCurrent())
                visibility = View.VISIBLE
            }
            Glide.with(binding.ivItemOnepicture)
                .load(item.photo)
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)

        }
    }

    inner class ItemChatOneEmojiReceiveVH(private val binding: ItemEmojiReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            Glide.with(binding.ivAvatar)
                .load(receiver?.profilePicture)
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_default)
                .error(R.drawable.ic_avatar_default)
                .into(binding.ivAvatar)
            binding.tvDate.apply {
                text = convertToMinuteSecond(getTimeCurrent())
                visibility = View.VISIBLE
            }
            Glide.with(binding.ivItemOnepicture)
                .load(item.emoji?.let { getEmoji(it.toInt()) })
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)

        }
    }

    inner class ItemChatOneEmojiSendVH(private val binding: ItemEmojiSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            Glide.with(binding.ivItemOnepicture)
                .load(item.emoji?.let { getEmoji(it.toInt()) })
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)
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
            RECEIVE_EMOJI->{
                val mHolder = holder as ItemChatOneEmojiReceiveVH
                mHolder.bind(chat)
            }
            SEND_EMOJI->{
                val mHolder = holder as ItemChatOneEmojiSendVH
                mHolder.bind(chat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = getItem(position)
        return if (chat.idSender == user?.uid) {
            when (chat.type) {
                MessageType.PHOTO -> return SEND_PHOTOS

                MessageType.TEXT -> return SEND_TEXT

                else -> return SEND_EMOJI

            }
        } else {
            when (chat.type) {
                MessageType.TEXT -> return RECEIVE_TEXT

                MessageType.PHOTO -> return RECEIVE_PHOTOS

                else -> return RECEIVE_EMOJI
            }
        }
    }
}