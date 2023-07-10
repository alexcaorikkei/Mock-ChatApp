package com.example.baseproject.ui.home.detailchat

import android.net.Uri

data class Chat(
    val id: String,
    val isSendText: Int? = null,
    val isSendPhoto: Int? = null,
    val isSendMultiPhoto: Int? = null,
    val date: String,

    val text: String? = null,
    val photoList: ArrayList<Uri>? = null,
)