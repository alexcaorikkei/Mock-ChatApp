package com.example.baseproject.ui.home.messages.model

import com.example.baseproject.domain.model.MessageType

data class MessageModel(
    val id: String = "",
    val name: String = "",
    val profilePicture: String = "",
    val lastMessageType: MessageType = MessageType.TEXT,
    val lastMessage: String = "",
    val time: String = "",
    val isSeen: Boolean = false,
)