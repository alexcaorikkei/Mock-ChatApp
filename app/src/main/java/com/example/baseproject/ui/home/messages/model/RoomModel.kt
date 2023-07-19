package com.example.baseproject.ui.home.messages.model
import com.example.baseproject.domain.model.MessageType

data class RoomModel(
    val id: String = "",
    val friendId: String = "",
    val name: String = "",
    val profilePicture: String = "",
    val lastMessageType: MessageType = MessageType.TEXT,
    val lastMessage: String = "",
    val time: String = "",
    val isSeen: Boolean = false,
    val isSent: Boolean = false
)