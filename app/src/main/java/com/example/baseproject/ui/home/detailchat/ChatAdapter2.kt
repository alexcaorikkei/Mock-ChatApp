package com.example.baseproject.ui.home.detailchat

import com.example.baseproject.databinding.ItemChatReceiveBinding
import com.example.baseproject.databinding.ItemChatSendBinding
import com.example.baseproject.databinding.ItemMultiphotoReceiveBinding
import com.example.baseproject.databinding.ItemMultiphotoSendBinding
import com.example.baseproject.databinding.ItemPhotoReceiveBinding
import com.example.baseproject.databinding.ItemPhotoSendBinding
import com.example.baseproject.extension.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.ItemChatDateBinding

enum class ChatAction2(val original: Int) {
    RECEIVE(NO_SEND_TEXT), SEND(SEND_TEXT), RECEIVE_PHOTO(NO_SEND_PHOTOS), SEND_PHOTO(SEND_PHOTOS),
    SEND_MULTIPHOTO(SEND_MULTIPHOTOS), RECEIVE_MULTIPHOTO(NO_SEND_MULTIPHOTOS), DATE(6)
}

class ChatAdapter2(
) : ListAdapter<Chat, RecyclerView.ViewHolder>(
    ExampleListDiffUtil()
) {

    override fun submitList(list: MutableList<Chat>?) {
        val result = arrayListOf<Chat>()
        list?.forEach {
            result.add(it.copy())
        }
        super.submitList(result)
    }

    class ItemChatDateVH(private val binding: ItemChatDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvDate.text = convertToDay(item.date)
        }
    }

    class ItemChatReceiveVH(private val binding: ItemChatReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvDate.apply {
                visibility = View.VISIBLE
                text = convertToMinuteSecond(item.date)
            }
            binding.tvItemchatMess.text = item.text
            binding.tvItemchatMess.text = item.text
        }
    }

    class ItemChatOnePhotoSendVH(private val binding: ItemPhotoSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvDate.apply {
                visibility = View.VISIBLE
                text = convertToMinuteSecond(item.date)
            }

            if (item.photoList?.size!! > 0) {
                Glide.with(binding.ivItemOnepicture)
                    .load(item.photoList[0])
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                    .into(binding.ivItemOnepicture)

            }
        }
    }

    class ItemChatSendVH(private val binding: ItemChatSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvItemSendMess.text = item.text
            binding.tvDate.apply {
                visibility = View.VISIBLE
                text = convertToMinuteSecond(item.date)
            }
        }
    }

    class ItemChatOnePhotoReceiveVH(private val binding: ItemPhotoReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.tvDate.apply {
                visibility = View.VISIBLE
                text = convertToMinuteSecond(item.date)
            }
            Glide.with(binding.ivItemOnepicture)
                .load(item.photoList?.get(0))
//                .override(
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
//                    (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
//                )
                .into(binding.ivItemOnepicture)
        }
    }

    class ItemChatMultiPhotoSendVH(private val binding: ItemMultiphotoSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            val pictureAdapter =
                PhotoDisplayAdapter(item.photoList ?: listOf())
            binding.tvDate.apply {
                visibility = View.VISIBLE
                text = convertToMinuteSecond(item.date)
            }
            binding.recycler.apply {
                adapter = pictureAdapter

                if (item.photoList!!.size > 2) {
                    layoutManager = GridLayoutManager(context, 3)
                    val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 3)
                    binding.recycler.addItemDecoration(divider)
                } else {
                    layoutManager = GridLayoutManager(context, 2)
                    val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 2)
                    binding.recycler.addItemDecoration(divider)
                }

            }
            pictureAdapter.notifyDataSetChanged()
        }
    }

    class ItemChatMultiPhotoReceiveVH(private val binding: ItemMultiphotoReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            val pictureAdapter =
                PhotoDisplayAdapter(item.photoList ?: listOf())
            binding.tvDate.apply {
                visibility = View.VISIBLE
                text = convertToMinuteSecond(item.date)
            }
            binding.recycler.apply {
                adapter = pictureAdapter

                if (item.photoList!!.size > 2) {
                    layoutManager = GridLayoutManager(context, 3)
                    val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 3)
                    binding.recycler.addItemDecoration(divider)
                } else {
                    layoutManager = GridLayoutManager(context, 2)
                    val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 2)
                    binding.recycler.addItemDecoration(divider)
                }

            }
            pictureAdapter.notifyDataSetChanged()
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
            ChatAction2.DATE.original -> {
                val binding =
                    ItemChatDateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatDateVH(binding)
            }

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

            ChatAction2.SEND_PHOTO.original -> {
                val binding =
                    ItemPhotoSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatOnePhotoSendVH(binding)
            }

            ChatAction2.RECEIVE_MULTIPHOTO.original -> {
                val binding =
                    ItemMultiphotoReceiveBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatMultiPhotoReceiveVH(binding)
            }

            else -> {
                val binding =
                    ItemMultiphotoSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ItemChatMultiPhotoSendVH(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)
        when (holder.itemViewType) {
            ChatAction2.DATE.original -> {
                val mHolder = holder as ItemChatDateVH
                mHolder.bind(chat)
            }

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

            ChatAction2.SEND_MULTIPHOTO.original -> {
                val mHolder = holder as ItemChatMultiPhotoSendVH
                mHolder.bind(chat)
            }

            ChatAction2.RECEIVE_MULTIPHOTO.original -> {
                val mHolder = holder as ItemChatMultiPhotoReceiveVH
                mHolder.bind(chat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        if (getItem(position).date.contains(",")) {
//            return ChatAction2.DATE.original
//        }
        if (getItem(position).isSendText == NO_SEND_TEXT) {
            return ChatAction2.RECEIVE.original
        }
        if (getItem(position).isSendText == SEND_TEXT) {
            return ChatAction2.SEND.original
        }
        if (getItem(position).isSendPhoto == NO_SEND_PHOTOS) {
            return ChatAction2.RECEIVE_PHOTO.original
        }
        if (getItem(position).isSendPhoto == SEND_PHOTOS) {
            return ChatAction2.SEND_PHOTO.original
        }
        if (getItem(position).isSendMultiPhoto == SEND_MULTIPHOTOS) {
            return ChatAction2.SEND_MULTIPHOTO.original
        }
        return ChatAction2.RECEIVE_MULTIPHOTO.original
    }
}