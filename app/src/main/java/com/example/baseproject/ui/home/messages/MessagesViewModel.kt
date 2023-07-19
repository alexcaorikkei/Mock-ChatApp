package com.example.baseproject.ui.home.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.RoomRepository
import com.example.baseproject.ui.home.messages.model.RoomModel
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val roomRepository: RoomRepository
) : BaseViewModel() {
    private val _roomResponse = roomRepository.getMessages()
    val roomResponse: LiveData<Response<List<RoomModel>>> = _roomResponse

    private val _searchResponse = MutableLiveData<Response<List<RoomModel>>>()
    val searchResponse: LiveData<Response<List<RoomModel>>> = _searchResponse

    fun searchMessages(query: String) {
        viewModelScope.launch {
            _searchResponse.postValue(Response.Loading)
            _searchResponse.postValue(roomRepository.searchMessages(query))
        }
    }
}