package com.example.baseproject.ui.home.detailchat

import com.example.baseproject.databinding.ItemChatReceiveBinding
import com.example.baseproject.databinding.ItemChatSendBinding
import com.example.baseproject.databinding.ItemMultiphotoReceiveBinding
import com.example.baseproject.databinding.ItemMultiphotoSendBinding
import com.example.baseproject.databinding.ItemPhotoReceiveBinding
import com.example.baseproject.databinding.ItemPhotoSendBinding
import com.example.baseproject.extension.*
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

enum class ChatAction(val original: Int) {
    RECEIVE(NO_SEND_TEXT), SEND(SEND_TEXT), RECEIVE_PHOTO(NO_SEND_PHOTOS), SEND_PHOTO(SEND_PHOTOS),
    SEND_MULTIPHOTO(SEND_MULTIPHOTOS), RECEIVE_MULTIPHOTO(NO_SEND_MULTIPHOTOS)
}

class ChatAdapter(

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listMessageChat: List<Chat> = ArrayList()
    class ItemChatReceiveVH(
        val bindingReceive: ItemChatReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceive.root)

    class ItemChatSendVH(
        val bindingSend: ItemChatSendBinding
    ) : RecyclerView.ViewHolder(bindingSend.root)

    class ItemChatOnePhotoSendVH(
        val bindingSendPhoto: ItemPhotoSendBinding
    ) : RecyclerView.ViewHolder(bindingSendPhoto.root)

    class ItemChatOnePhotoReceiveVH(
        val bindingReceivePhoto: ItemPhotoReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceivePhoto.root)

    class ItemChatMultiPhotoSendVH(
        val bindingMultiPhotoSend: ItemMultiphotoSendBinding
    ) : RecyclerView.ViewHolder(bindingMultiPhotoSend.root)

    class ItemChatMultiPhotoReceiveVH(
        val bindingMultiPhotoReceive: ItemMultiphotoReceiveBinding
    ) : RecyclerView.ViewHolder(bindingMultiPhotoReceive.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ChatAction.RECEIVE.original) return ItemChatReceiveVH(
            ItemChatReceiveBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        if (viewType == ChatAction.SEND.original) return ItemChatSendVH(
            ItemChatSendBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        if (viewType == ChatAction.RECEIVE_PHOTO.original)
            return ItemChatOnePhotoReceiveVH(
                ItemPhotoReceiveBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        if (viewType == ChatAction.SEND_PHOTO.original)
            return ItemChatOnePhotoSendVH(
                ItemPhotoSendBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        if (viewType == ChatAction.RECEIVE_MULTIPHOTO.original)
            return ItemChatMultiPhotoReceiveVH(
                ItemMultiphotoReceiveBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        return ItemChatMultiPhotoSendVH(
            ItemMultiphotoSendBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ChatAction.RECEIVE.original -> {
                val userFeatureViewHolder = holder as ItemChatReceiveVH
                userFeatureViewHolder.bindingReceive.tvItemchatMess.text =
                    listMessageChat[position].text
            }

            ChatAction.SEND.original -> {
                val userFeatureViewHolder = holder as ItemChatSendVH
                userFeatureViewHolder.bindingSend.tvItemSendMess.text =
                    listMessageChat[position].text
            }

            ChatAction.RECEIVE_PHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatOnePhotoReceiveVH
                Glide.with(userFeatureViewHolder.bindingReceivePhoto.ivItemOnepicture)
                    .load(listMessageChat[position].photoList?.get(0))
                    .override(
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
                    )
                    .into(userFeatureViewHolder.bindingReceivePhoto.ivItemOnepicture)
            }

            ChatAction.SEND_PHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatOnePhotoSendVH
                Glide.with(userFeatureViewHolder.bindingSendPhoto.ivItemOnepicture)
                    .load(listMessageChat[position].photoList?.get(0))
                    .override(
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
                    )
                    .into(userFeatureViewHolder.bindingSendPhoto.ivItemOnepicture)
            }

            ChatAction.SEND_MULTIPHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatMultiPhotoSendVH
                val pictureAdapter =
                    PhotoDisplayAdapter(listMessageChat[position].photoList ?: listOf())
                userFeatureViewHolder.bindingMultiPhotoSend.recycler.apply {
                    adapter = pictureAdapter

                    if (listMessageChat[position].photoList!!.size > 2) {
                        layoutManager = GridLayoutManager(context, 3)
                        val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 3)
                        userFeatureViewHolder.bindingMultiPhotoSend.recycler.addItemDecoration(divider)
                    } else {
                        layoutManager = GridLayoutManager(context, 2)
                        val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 2)
                        userFeatureViewHolder.bindingMultiPhotoSend.recycler.addItemDecoration(divider)
                    }

                }
                pictureAdapter.notifyDataSetChanged()
            }

            ChatAction.RECEIVE_MULTIPHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatMultiPhotoReceiveVH
                val pictureAdapter =
                    PhotoDisplayAdapter(listMessageChat[position].photoList ?: listOf())
                userFeatureViewHolder.bindingMultiPhotoReceive.recycler.apply {
                    adapter = pictureAdapter

                    if (listMessageChat[position].photoList!!.size > 2) {
                        layoutManager = GridLayoutManager(context, 3)
                        val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 3)
                        userFeatureViewHolder.bindingMultiPhotoReceive.recycler.addItemDecoration(divider)
                    } else {
                        layoutManager = GridLayoutManager(context, 2)
                        val divider = GridItemSpacingDecoration(convertDpToPixel(context, 4), 2)
                        userFeatureViewHolder.bindingMultiPhotoReceive.recycler.addItemDecoration(divider)
                    }

                }
                pictureAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return listMessageChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listMessageChat[position].isSendText == NO_SEND_TEXT) {
            return ChatAction.RECEIVE.original
        }
        if (listMessageChat[position].isSendText == SEND_TEXT) {
            return ChatAction.SEND.original
        }
        if (listMessageChat[position].isSendPhoto == NO_SEND_PHOTOS) {
            return ChatAction.RECEIVE_PHOTO.original
        }
        if (listMessageChat[position].isSendPhoto == SEND_PHOTOS) {
            return ChatAction.SEND_PHOTO.original
        }
        if (listMessageChat[position].isSendMultiPhoto == SEND_MULTIPHOTOS) {
            return ChatAction.SEND_MULTIPHOTO.original
        }
        return ChatAction.RECEIVE_MULTIPHOTO.original
    }
}