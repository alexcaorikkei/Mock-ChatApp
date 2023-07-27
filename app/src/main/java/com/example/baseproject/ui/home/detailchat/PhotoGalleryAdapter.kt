package com.example.baseproject.ui.home.detailchat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.ItemPhotoBinding

class PhotoAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.ChatVH>() {
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
        val photo = photoList[position]
        with(holder.binding) {
            cbItemClicked.isChecked = photo.isClicked

            Glide.with(ivItemPicture)
                .load(photo.uri)
                .into(ivItemPicture)

            root.setOnClickListener {
                onClickListener?.pickPhoto(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}

interface OnPhotoAdapterListener {
    fun pickPhoto(position: Int)
}