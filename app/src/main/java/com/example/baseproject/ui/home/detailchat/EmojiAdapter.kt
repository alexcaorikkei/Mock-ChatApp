package com.example.baseproject.ui.home.detailchat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemEmojiBinding
import com.example.baseproject.databinding.ItemPhotoBinding

class EmojiAdapter(var emojiList: ArrayList<Emoji>) :
    RecyclerView.Adapter<EmojiAdapter.ChatVH>() {
    var onClickListener: OnEmojiAdapterListener? = null

    class ChatVH(
        val binding: ItemEmojiBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        return ChatVH(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        val emoji = emojiList[position]
        Glide.with(holder.binding.ivItemEmoji)
            .load(getEmoji(emoji.content))
            .placeholder(R.drawable.ic_avatar_default)
            .error(R.drawable.ic_avatar_default)
            .into(holder.binding.ivItemEmoji)

        holder.binding.root.setOnClickListener {
            onClickListener?.pickEmoji(position)
        }
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }
}
interface OnEmojiAdapterListener {
    fun pickEmoji(position: Int)
}