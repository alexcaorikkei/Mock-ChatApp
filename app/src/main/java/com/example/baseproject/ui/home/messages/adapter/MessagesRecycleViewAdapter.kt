package com.example.baseproject.ui.home.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemMessageBinding
import com.example.baseproject.ui.home.messages.MessageModel

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
//            ivAvatar.setImageResource(messageData.image)
            tvName.text = messageData.name
            tvMessage.text = messageData.message
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

