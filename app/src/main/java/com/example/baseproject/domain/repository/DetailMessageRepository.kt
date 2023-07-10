package com.example.baseproject.domain.repository

import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.detailchat.Chat

interface DetailMessageRepository {
    suspend fun sendMessage(chat: Chat, idReceive: String): Response<Boolean>
    suspend fun sendPhoto(chat: Chat, idReceive: String): Response<Boolean>
}