package com.example.baseproject.ui.home.detailchat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemPhotoBinding

class EmojiAdapter(var emojiList: ArrayList<Emoji>) :
    RecyclerView.Adapter<EmojiAdapter.ChatVH>() {
    var onClickListener: OnPhotoAdapterListener? = null

    class ChatVH(
        val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        return ChatVH(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        val emoji = emojiList[position]
        holder.binding.cbItemClicked.isChecked = emoji.isClicked

        Glide.with(holder.binding.ivItemPicture)
            .load(getEmoji(emoji.content))
            .placeholder(R.drawable.ic_avatar_default)
            .error(R.drawable.ic_avatar_default)
            .into(holder.binding.ivItemPicture)

        holder.binding.cbItemClicked.setOnClickListener {
            onClickListener?.pickPhoto(position)
        }
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }
}