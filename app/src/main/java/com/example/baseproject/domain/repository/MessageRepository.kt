package com.example.baseproject.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.messages.model.RoomModel

interface MessageRepository {
    fun getMessages(): MutableLiveData<Response<List<RoomModel>>>
}