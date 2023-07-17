package com.example.baseproject.ui.home.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.ItemMessageBinding
import com.example.baseproject.ui.home.messages.model.MessageModel

class MessageHolder(var binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root) {

}

class MessageAdapter(private val listMessages: List<MessageModel>): RecyclerView.Adapter<MessageHolder>() {
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
            tvMessage.text = messageData.lastMessage
            tvTime.text = messageData.time
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

