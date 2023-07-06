package com.example.baseproject.domain.repository

import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.friends.model.FriendModel

interface FriendRepository {
    suspend fun searchAllUserWithCurrentAccount(query: String): Response<List<FriendModel>>
    suspend fun sendFriendRequest(friendId: String): Response<String>
    suspend fun acceptFriendRequest(friendId: String): Response<String>
    suspend fun cancelFriendRequest(friendId: String): Response<String>
}