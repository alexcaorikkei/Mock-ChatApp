package com.example.baseproject.ui.home.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemFriendBinding
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.baseproject.ui.home.friends.model.FriendState

class FriendsRecycleViewHolder(var binding: ItemFriendBinding): RecyclerView.ViewHolder(binding.root) {

}

class FriendsRecycleViewAdapter(private val listFriends: List<FriendModel>): RecyclerView.Adapter<FriendsRecycleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsRecycleViewHolder {
        return FriendsRecycleViewHolder(
            ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun getItemCount() = listFriends.size
    override fun onBindViewHolder(holder: FriendsRecycleViewHolder, position: Int) {
        val friendData = listFriends[position]
        with(holder.binding) {
//           ivAvatar =
            tvName.text = friendData.displayName
            when(friendData.state) {
                FriendState.FRIEND -> {

                }
                FriendState.ADDED -> {
                    btnCancel.visibility = ViewGroup.VISIBLE
                }
                FriendState.REQUEST -> {
                    btnAccept.visibility = ViewGroup.VISIBLE
                }
                FriendState.NONE -> {
                    btnAddNewFriend.visibility = ViewGroup.VISIBLE
                }
            }
        }
    }
}

