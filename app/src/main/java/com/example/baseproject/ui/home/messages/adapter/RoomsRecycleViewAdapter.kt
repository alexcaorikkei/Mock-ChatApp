package com.example.baseproject.ui.home.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemRoomBinding
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.ui.home.messages.model.RoomModel

interface OnRoomClickListener {
    fun onMessageClicked(position: Int, view: ItemRoomBinding)
}

class RoomHolder(var binding: ItemRoomBinding, private val onRoomClickListener: OnRoomClickListener): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.apply {
            root.setOnClickListener {
                onRoomClickListener.onMessageClicked(adapterPosition, binding)
            }
        }
    }
}

class RoomAdapter(
    private val listRoom: List<RoomModel>,
    private val onRoomClickListener: OnRoomClickListener): RecyclerView.Adapter<RoomHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
        return RoomHolder(
            ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onRoomClickListener
        )
    }

    override fun getItemCount() = listRoom.size

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        val roomData = listRoom[position]
        with(holder.binding) {
            if(listRoom[position].profilePicture.isNotEmpty()) {
                Glide.with(ivAvatar.context)
                    .load(listRoom[position].profilePicture.toUri())
                    .into(ivAvatar)
                    .onLoadStarted(getDrawable(ivAvatar.context, com.example.core.R.drawable.ic_avatar_default))
            } else {
                Glide.with(ivAvatar.context)
                    .load(getDrawable(ivAvatar.context, com.example.core.R.drawable.ic_avatar_default))
                    .into(ivAvatar)
            }
            tvName.text = roomData.name
            tvMessage.text = when(roomData.messageType) {
                MessageType.TEXT -> {
                    if(roomData.messagesMatched <= 1) {
                        if(roomData.isSent) {
                            "${tvMessage.context.getString(R.string.you)}: ${roomData.message}"
                        } else {
                            roomData.message
                        }
                    } else {
                        "${roomData.messagesMatched} ${tvMessage.context.getString(R.string.messages_matched)}"
                    }
                }
                MessageType.NONE -> {""}
                MessageType.PHOTO -> {
                    if(roomData.isSent) {
                        "${tvMessage.context.getString(R.string.you)} ${tvMessage.context.getString(R.string.sent_a_photo)}"
                    } else {
                        "${roomData.name.split(" ").last()} ${tvMessage.context.getString(R.string.sent_a_photo)}"
                    }
                }
                MessageType.EMOJI -> {
                    if(roomData.isSent) {
                        "${tvMessage.context.getString(R.string.you)} ${tvMessage.context.getString(R.string.sent_a_emoji)}"
                    } else {
                        "${roomData.name.split(" ").last()} ${tvMessage.context.getString(R.string.sent_a_emoji)}"
                    }
                }
                MessageType.DATE -> {""}
            }
            if(roomData.messagesMatched == 0) {
                try {
                    tvTime.text = tvTime.context.getString(roomData.time.toInt())
                } catch (e: Exception) {
                    tvTime.text = roomData.time
                }
            } else {
                tvTime.text = ""
            }
            if (roomData.isSeen) {
                tvMessage.setTextColor(tvMessage.context.getColor(com.example.core.R.color.gray))
            } else {
                tvMessage.setTextColor(tvMessage.context.getColor(android.R.color.black))
            }
            if(position == listRoom.lastIndex) {
                holder.binding.ivLine.visibility = android.view.View.GONE
            } else {
                holder.binding.ivLine.visibility = android.view.View.VISIBLE
            }
        }
    }
}

