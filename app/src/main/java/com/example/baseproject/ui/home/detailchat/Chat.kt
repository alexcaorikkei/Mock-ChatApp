package com.example.baseproject.ui.home.detailchat

data class Chat(
    val id: String,
    val idSender: String,
    val date: String,
    val description: String? = null
) {
    constructor () : this("", "", "", "")
}