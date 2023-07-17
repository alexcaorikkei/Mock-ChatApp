package com.example.baseproject.domain.repository

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.FriendModel

interface DetailMessageRepository {
    suspend fun sendMessage(chatModel: ChatModel, idReceive: String): Response<Boolean>
    suspend fun sendPhoto(chatModel: ChatModel, idReceive: String): Response<Boolean>
    suspend fun getInformationReceiver(idReceive: String): Response<FriendModel>
    suspend fun getListMessage(idReceive: String): Response<List<ChatModel>>
}