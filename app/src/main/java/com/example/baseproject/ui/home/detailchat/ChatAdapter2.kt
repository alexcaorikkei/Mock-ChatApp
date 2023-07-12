package com.example.baseproject.ui.home.detailchat

import com.example.baseproject.databinding.ItemChatReceiveBinding
import com.example.baseproject.databinding.ItemChatSendBinding
import com.example.baseproject.databinding.ItemPhotoReceiveBinding
import com.example.baseproject.databinding.ItemPhotoSendBinding
import com.example.baseproject.extension.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

enum class ChatAction2(val original: Int) {
    RECEIVE(RECEIVE_TEXT), SEND(SEND_TEXT), RECEIVE_PHOTO(RECEIVE_PHOTOS), SEND_PHOTO(SEND_PHOTOS)
}

class ChatAdapter2(
) : ListAdapter<Chat, RecyclerView.ViewHolder>(
    ExampleListDiffUtil()
) {
    private var uId: String? = null
    fun setUid(uId: String?) {
        this.uId = uId
    }

    override fun submitList(list: MutableList<Chat>?) {
        val result = arrayListOf<Chat>()
        list?.forEach {
            result.add(it.copy())
        }
        super.submitList(result)
    }

    class ItemChatReceiveVH(private val binding: ItemChatReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvDate.apply {
                text = convertToMinuteSecond(getTimeCurrent())
                visibility = View.VISIBLE
            }
            binding.tvItemchatMess.text = item.description
        }
    }

    class ItemChatOnePhotoSendVH(private val binding: ItemPhotoSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            Glide.with(binding.ivItemOnepicture)
                .load(item.description)
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)
        }
    }

    class ItemChatSendVH(private val binding: ItemChatSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvItemSendMess.text = item.description
//            binding.tvItemSendMess.setOnClickListener {
//                text = convertToMinuteSecond(getTimeCurrent())
//                visibility = View.VISIBLE
//            }
        }
    }


    class ItemChatOnePhotoReceiveVH(private val binding: ItemPhotoReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvDate.apply {
                text = convertToMinuteSecond(getTimeCurrent())
                visibility = View.VISIBLE
            }
            Glide.with(binding.ivItemOnepicture)
                .load(item.description)
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)
        }
    }

    class ExampleListDiffUtil : DiffUtil.ItemCallback<Chat>() {
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat) =
            oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ChatAction2.RECEIVE.original -> {
                val binding =
                    ItemChatReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatReceiveVH(binding)
            }

            ChatAction2.SEND.original -> {
                val binding =
                    ItemChatSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatSendVH(binding)
            }

            ChatAction2.RECEIVE_PHOTO.original -> {
                val binding =
                    ItemPhotoReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOnePhotoReceiveVH(binding)
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
            ChatAction2.RECEIVE.original -> {
                val mHolder = holder as ItemChatReceiveVH
                mHolder.bind(chat)
            }

            ChatAction2.SEND.original -> {
                val mHolder = holder as ItemChatSendVH
                mHolder.bind(chat)
            }

            ChatAction2.RECEIVE_PHOTO.original -> {
                val mHolder = holder as ItemChatOnePhotoReceiveVH
                mHolder.bind(chat)
            }

            ChatAction2.SEND_PHOTO.original -> {
                val mHolder = holder as ItemChatOnePhotoSendVH
                mHolder.bind(chat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = getItem(position)

        return if (chat.idSender == uId) {
            if (chat.description?.startsWith("https://") == true) {
                SEND_PHOTOS
            } else {
                SEND_TEXT
            }
        } else if (chat.description?.startsWith("https://") == true) {
            RECEIVE_PHOTOS
        } else {
            RECEIVE_TEXT
        }
    }
}