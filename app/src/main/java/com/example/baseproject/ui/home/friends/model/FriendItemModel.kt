package com.example.baseproject.ui.home.friends.model

import android.content.res.Resources
import com.example.baseproject.R

enum class SortType {
    SORT_BY_NAME,
    SORT_BY_STATE
}

fun getFromListFriendModelSortBy(sortType: SortType, listFriend : List<FriendModel>): List<FriendItemModel> {
    if(listFriend.isEmpty()) return listOf()
    return when(sortType) {
        SortType.SORT_BY_NAME -> {
            val result = mutableListOf<FriendItemModel>()
            val sortedList = listFriend.sortedBy { it.displayName.split(" ").last() }
            var currentHeader = 'A'
            if(sortedList.first().displayName.split(" ").last().first() == currentHeader) {
                result.add(FriendItemModel(1, header = currentHeader.toString()))
            }
            sortedList.forEach() {friend ->
            if (friend.displayName.split(" ").last().first() != currentHeader) {
                    currentHeader = friend.displayName.split(" ").last().first()
                    result.add(FriendItemModel(1, header = currentHeader.toString()))
                }
                result.add(FriendItemModel(0, friendModel = friend))
            }
            result
        }
        SortType.SORT_BY_STATE -> {
            val result = mutableListOf<FriendItemModel>()
            val listFriendRequest = listFriend.filter { friendModel ->
                friendModel.state == FriendState.REQUEST
            }
            val listFriendAdded = listFriend.filter { friendModel ->
                friendModel.state == FriendState.ADDED
            }
            result.add(FriendItemModel(1, header = R.string.friend_request.toString()))
            listFriendRequest.forEach { friend ->
                result.add(FriendItemModel(0, friendModel = friend))
            }
            result.add(FriendItemModel(1, header = R.string.friend_added.toString()))
            listFriendAdded.forEach { friend ->
                result.add(FriendItemModel(0, friendModel = friend))
            }
            result
        }
    }
}

data class FriendItemModel(
    val viewType: Int,
    val friendModel: FriendModel? = null,
    val header: String? = null
)