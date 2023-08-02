package com.example.baseproject.ui.home.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.ItemFriendBinding
import com.example.baseproject.databinding.ItemFriendHeaderBinding
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.extension.gone
import com.example.baseproject.extension.invisible
import com.example.baseproject.extension.toViWithoutAccent
import com.example.baseproject.extension.visible
import com.example.baseproject.ui.home.friends.model.FriendItemModel
import com.example.baseproject.ui.home.friends.model.SortType
import com.example.baseproject.ui.home.friends.model.getFromListFriendModelSortBy
import com.example.core.R
import timber.log.Timber

interface OnItemClickListener {
    fun onItemClicked(friendData: FriendModel)
    fun onAcceptClicked(friendData: FriendModel)
    fun onCancelClicked(friendData: FriendModel)
    fun onAddNewFriendClicked(friendData: FriendModel)
}

class FriendsRecycleViewHolder(
    var binding: ItemFriendBinding,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
     fun bind(friendData: FriendModel) {
        binding.apply {
            root.setOnClickListener {
                onItemClickListener.onItemClicked(friendData)
            }

            btnAccept.setOnClickListener {
                onItemClickListener.onAcceptClicked(friendData)
            }

            btnCancel.setOnClickListener {
                onItemClickListener.onCancelClicked(friendData)
            }

            btnAddNewFriend.setOnClickListener {
                onItemClickListener.onAddNewFriendClicked(friendData)
            }

            // load image avatar if display name is not empty
            if(friendData.displayName.isNotEmpty()) {
                Glide.with(root.context)
                    .load(friendData.profilePicture.toUri())
                    .placeholder(R.drawable.ic_avatar_default)
                    .error(R.drawable.ic_avatar_default)
                    .into(ivAvatar)
            } else {
                ivAvatar.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        R.drawable.ic_avatar_default
                    )
                )
            }

            tvName.text = friendData.displayName
            when(friendData.state) {
                FriendState.FRIEND -> {
                    btnAddNewFriend.invisible()
                    btnAccept.invisible()
                    btnCancel.invisible()
                }
                FriendState.ADDED -> {
                    btnAddNewFriend.invisible()
                    btnAccept.invisible()
                    btnCancel.visible()
                }
                FriendState.REQUEST -> {
                    btnAddNewFriend.invisible()
                    btnAccept.visible()
                    btnCancel.invisible()
                }
                FriendState.NONE -> {
                    btnAddNewFriend.visible()
                    btnAccept.invisible()
                    btnCancel.invisible()
                }
            }
        }


    }

}

class FriendHeaderViewHolder(var binding: ItemFriendHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(header: String) {
        binding.apply {
            tvHeader.text = if(header.length == 1) {
                 header
            } else {
                tvHeader.context.getString(header.toInt())
            }
        }
    }
}

object FriendItemDiffUtil : DiffUtil.ItemCallback<FriendItemModel>() {
    override fun areContentsTheSame(oldItem: FriendItemModel, newItem: FriendItemModel) =
        oldItem == newItem


    override fun areItemsTheSame(oldItem: FriendItemModel, newItem: FriendItemModel) =
        if(oldItem.viewType == 0 && newItem.viewType == 0) {
            oldItem.friendModel!!.uid == newItem.friendModel!!.uid
        } else {
            oldItem.header == newItem.header
        }
}

class FriendsRecycleViewAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val _listFriend: List<FriendModel>,
    private val sortType: SortType
) : ListAdapter<FriendItemModel, RecyclerView.ViewHolder>(FriendItemDiffUtil) {

    override fun submitList(list: List<FriendItemModel>?) {
        val result = arrayListOf<FriendItemModel>()
        list?.forEach {
            result.add(it.clone())
        }
        super.submitList(result)
    }

    fun notifyChangeList() {
        submitList(getFromListFriendModelSortBy(sortType, _listFriend))
    }

    fun filter(query: String): Boolean {
        val newList = getFromListFriendModelSortBy(
        sortType,
        _listFriend.filter {
            it.displayName.toViWithoutAccent().contains(query.toViWithoutAccent())
        }
        )
        submitList(newList)
        return newList.isNotEmpty()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
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
        when(holder) {
            is FriendsRecycleViewHolder -> holder.bind(getItem(position).friendModel!!)
            is FriendHeaderViewHolder -> holder.bind(getItem(position).header!!)
        }
    }
}

