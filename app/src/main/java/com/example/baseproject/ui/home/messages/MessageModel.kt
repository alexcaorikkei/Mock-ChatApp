package com.example.baseproject.ui.home.messages

data class MessageModel(
    val id: Int,
    val name: String,
    val message: String,
    val time: String,
    val image: String,
    val isSeen: Boolean
)