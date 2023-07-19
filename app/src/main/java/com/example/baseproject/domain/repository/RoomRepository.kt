package com.example.baseproject.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.messages.model.RoomModel

interface RoomRepository {
    fun getMessages(): MutableLiveData<Response<List<RoomModel>>>
    suspend fun searchMessages(query: String): Response<List<RoomModel>>
}