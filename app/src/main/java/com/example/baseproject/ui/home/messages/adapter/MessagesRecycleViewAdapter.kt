package com.example.baseproject.ui.home.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemMessageBinding
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.ui.home.messages.model.RoomModel

class MessageHolder(var binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root) {

}

class MessageAdapter(private val listMessages: List<RoomModel>): RecyclerView.Adapter<MessageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listMessages.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val messageData = listMessages[position]
        with(holder.binding) {
            if(listMessages[position].profilePicture.isNotEmpty()) {
                Glide.with(ivAvatar.context)
                    .load(listMessages[position].profilePicture.toUri())
                    .into(ivAvatar)
                    .onLoadStarted(getDrawable(ivAvatar.context, com.example.core.R.drawable.ic_avatar_default))
            }
            tvName.text = messageData.name
            tvMessage.text = when(messageData.lastMessageType) {
                MessageType.TEXT -> {
                    if(messageData.isSent) {
                        "${tvMessage.context.getString(R.string.you)}: ${messageData.lastMessage}"
                    } else {
                        messageData.lastMessage
                    }
                }
                MessageType.NONE -> {""}
                MessageType.PHOTO -> {
                    if(messageData.isSent) {
                        "${tvMessage.context.getString(R.string.you)} ${tvMessage.context.getString(R.string.sent_a_photo)}"
                    } else {
                        "${messageData.name.split(" ").last()} ${tvMessage.context.getString(R.string.sent_a_photo)}"
                    }
                }
                MessageType.EMOJI -> {
                    if(messageData.isSent) {
                        "${tvMessage.context.getString(R.string.you)} ${tvMessage.context.getString(R.string.sent_a_emoji)}"
                    } else {
                        "${messageData.name.split(" ").last()} ${tvMessage.context.getString(R.string.sent_a_emoji)}"
                    }
                }
                MessageType.DATE -> {""}
            }
            try {
                tvTime.text = tvTime.context.getString(messageData.time.toInt())
            } catch (e: Exception) {
                tvTime.text = messageData.time
            }
            if (messageData.isSeen) {
                tvMessage.setTextColor(tvMessage.context.getColor(com.example.core.R.color.gray))
            } else {
                tvMessage.setTextColor(tvMessage.context.getColor(android.R.color.black))
            }
            if(position == listMessages.lastIndex) {
                holder.binding.ivLine.visibility = android.view.View.GONE
            } else {
                holder.binding.ivLine.visibility = android.view.View.VISIBLE
            }
        }
    }
}

