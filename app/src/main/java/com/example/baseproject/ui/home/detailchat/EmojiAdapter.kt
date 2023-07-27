package com.example.baseproject.ui.home.detailchat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemEmojiBinding

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
        with(holder.binding) {
            Glide.with(ivItemEmoji)
                .load(getEmoji(emoji.content))
                .placeholder(R.drawable.ic_avatar_default)
                .error(R.drawable.ic_avatar_default)
                .into(ivItemEmoji)

            root.setOnClickListener {
                onClickListener?.pickEmoji(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }
}
interface OnEmojiAdapterListener {
    fun pickEmoji(position: Int)
}