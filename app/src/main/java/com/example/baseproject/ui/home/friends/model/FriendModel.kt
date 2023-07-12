package com.example.baseproject.ui.home.friends.model

enum class FriendState {
    FRIEND,
    ADDED,
    REQUEST,
    NONE;
    override fun toString(): String {
        return when(this) {
            FRIEND -> "friends"
            ADDED -> "added"
            REQUEST -> "request"
            NONE -> "none"
        }
    }
}

data class FriendModel(
    val uid: String = "",
    val displayName: String = "",
    val profilePicture: String = "",
    val state: FriendState = FriendState.NONE
)