package com.example.baseproject.ui.home.detailchat

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemMultiphotoReceiveOneBinding


class PhotoDisplayAdapter(private val photoList: List<Uri>) :
    RecyclerView.Adapter<PhotoDisplayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMultiphotoReceiveOneBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindingItemMultiPhoto.imageViewPhoto.setImageURI(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(
        val bindingItemMultiPhoto: ItemMultiphotoReceiveOneBinding
    ) : RecyclerView.ViewHolder(bindingItemMultiPhoto.root)

}