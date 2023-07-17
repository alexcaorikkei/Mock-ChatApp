package com.example.baseproject.ui.home.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.MessageRepository
import com.example.baseproject.ui.home.messages.model.RoomModel
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val messageRepository: MessageRepository
) : BaseViewModel() {
    private val _searchResponse = messageRepository.getMessages()
    val searchResponse: LiveData<Response<List<RoomModel>>> = _searchResponse

    fun searchRoom(query: String) {
        _searchResponse.value = Response.Success(
            (_searchResponse.value as Response.Success<List<RoomModel>>).data.filter { room ->
                room.name.contains(query, true)
            }
        )
    }

}