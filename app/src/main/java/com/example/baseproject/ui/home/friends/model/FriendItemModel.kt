package com.example.baseproject.ui.home.friends.model

import com.example.baseproject.R
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.extension.toViWithoutAccent

enum class SortType {
    SORT_BY_NAME,
    SORT_BY_STATE
}

fun getFromListFriendModelSortBy(sortType: SortType, listFriend : List<FriendModel>): MutableList<FriendItemModel> {
    if(listFriend.isEmpty()) return mutableListOf()
    return when(sortType) {
        SortType.SORT_BY_NAME -> {
            val result = mutableListOf<FriendItemModel>()
            val sortedList = listFriend.sortedBy { it.displayName.split(" ").last().toViWithoutAccent() }
            var currentHeader = 'A'
            if(sortedList.first().displayName.split(" ").last().toViWithoutAccent().first() == currentHeader) {
                result.add(FriendItemModel(1, header = currentHeader.toString()))
            }
            sortedList.forEach() {friend ->
            if (friend.displayName.split(" ").last().toViWithoutAccent().first() != currentHeader) {
                    currentHeader = friend.displayName.split(" ").last().toViWithoutAccent().first()
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
            if(result.last().viewType == 1) {
                result.removeLast()
            }
            result.add(FriendItemModel(1, header = R.string.friend_added.toString()))
            listFriendAdded.forEach { friend ->
                result.add(FriendItemModel(0, friendModel = friend))
            }
            if(result.last().viewType == 1) {
                result.removeLast()
            }
            result
        }
    }
}

data class FriendItemModel(
    val viewType: Int,
    var friendModel: FriendModel? = null,
    val header: String? = null
){
    fun clone() : FriendItemModel{
        val result = copy().apply {
            friendModel?.let {
                friendModel =  friendModel!!.copy()
            }
        }

        return result
    }
}
