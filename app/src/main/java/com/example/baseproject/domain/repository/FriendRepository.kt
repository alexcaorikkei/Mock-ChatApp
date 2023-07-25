package com.example.baseproject.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.model.FriendModel

interface FriendRepository {
    fun getFriends(): MutableLiveData<Response<List<FriendModel>>>

    suspend fun addFriend(friendUid: String): Response<Boolean>
    suspend fun acceptFriend(friendUid: String): Response<Boolean>
    suspend fun removeFriend(friendUid: String): Response<Boolean>
    suspend fun cancelFriend(friendUid: String): Response<Boolean>
}