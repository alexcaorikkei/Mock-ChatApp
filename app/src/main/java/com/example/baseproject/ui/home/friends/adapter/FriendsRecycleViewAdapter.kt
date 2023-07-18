package com.example.baseproject.ui.home.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.ItemFriendBinding
import com.example.baseproject.databinding.ItemFriendHeaderBinding
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.ui.home.friends.model.FriendItemModel
import com.example.core.R
import timber.log.Timber

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: ItemFriendBinding)
    fun onAcceptClicked(position: Int, view: ItemFriendBinding)
    fun onCancelClicked(position: Int, view: ItemFriendBinding)
    fun onAddNewFriendClicked(position: Int, view: ItemFriendBinding)
}

class FriendsRecycleViewHolder(
    var binding: ItemFriendBinding,
    onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.apply {
            root.setOnClickListener {
                onItemClickListener.onItemClicked(adapterPosition, binding)
            }

            btnAccept.setOnClickListener {
                onItemClickListener.onAcceptClicked(adapterPosition, binding)
            }

            btnCancel.setOnClickListener {
                onItemClickListener.onCancelClicked(adapterPosition, binding)
            }

            btnAddNewFriend.setOnClickListener {
                onItemClickListener.onAddNewFriendClicked(adapterPosition, binding)
            }
        }
    }
}

class FriendHeaderViewHolder(var binding: ItemFriendHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
}

class FriendsRecycleViewAdapter(
    private val listViewFriends: List<FriendItemModel>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = listViewFriends.size
    override fun getItemViewType(position: Int): Int {
        return listViewFriends[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = ItemFriendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                FriendsRecycleViewHolder(binding, onItemClickListener)
            }

            else -> {
                val binding = ItemFriendHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FriendHeaderViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FriendsRecycleViewHolder -> {
                val friendData = listViewFriends[position].friendModel
                Timber.d("onBindViewHolder: $position")
                Timber.d("onBindViewHolder: $friendData")
                holder.binding.apply {
                    tvName.text = friendData!!.displayName
                    if(friendData.profilePicture.isNotEmpty()) {
                        Glide.with(ivAvatar.context)
                            .load(friendData.profilePicture.toUri())
                            .into(ivAvatar)
                            .onLoadStarted(
                                AppCompatResources.getDrawable(
                                    ivAvatar.context,
                                    R.drawable.ic_avatar_default
                                )
                            )
                    } else {
                        Glide.with(ivAvatar.context)
                            .load(R.drawable.ic_avatar_default)
                            .into(ivAvatar)
                    }
                    when (friendData.state) {
                        FriendState.FRIEND -> {
                            btnCancel.visibility = ViewGroup.INVISIBLE
                            btnAccept.visibility = ViewGroup.INVISIBLE
                            btnAddNewFriend.visibility = ViewGroup.INVISIBLE
                        }

                        FriendState.ADDED -> {
                            btnCancel.visibility = ViewGroup.VISIBLE
                            btnAccept.visibility = ViewGroup.INVISIBLE
                            btnAddNewFriend.visibility = ViewGroup.INVISIBLE
                        }

                        FriendState.REQUEST -> {
                            btnCancel.visibility = ViewGroup.INVISIBLE
                            btnAccept.visibility = ViewGroup.VISIBLE
                            btnAddNewFriend.visibility = ViewGroup.INVISIBLE
                        }

                        FriendState.NONE -> {
                            btnCancel.visibility = ViewGroup.INVISIBLE
                            btnAccept.visibility = ViewGroup.INVISIBLE
                            btnAddNewFriend.visibility = ViewGroup.VISIBLE
                        }
                    }
                }
            }

            is FriendHeaderViewHolder -> {
                if (listViewFriends[position].header!!.length == 1) {
                    holder.binding.tvHeader.text = listViewFriends[position].header
                } else {
                    holder.binding.tvHeader.text =
                        holder.binding.tvHeader.context.getString(listViewFriends[position].header!!.toInt())
                }
            }
        }
    }
}

