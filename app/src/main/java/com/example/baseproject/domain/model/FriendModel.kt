package com.example.baseproject.domain.model

enum class FriendState {
    FRIEND,
    ADDED,
    REQUEST,
    NONE;
    override fun toString(): String {
        return when(this) {
            FRIEND -> "friend"
            ADDED -> "added"
            REQUEST -> "request"
            NONE -> "none"
        }
    }

    companion object {
        fun fromString(string: String): FriendState {
            return when(string) {
                "friend" -> FRIEND
                "added" -> ADDED
                "request" -> REQUEST
                else -> NONE
            }
        }
    }
}

data class FriendModel(
    val uid: String = "",
    val displayName: String = "",
    val profilePicture: String = "",
    var state: FriendState = FriendState.NONE
)