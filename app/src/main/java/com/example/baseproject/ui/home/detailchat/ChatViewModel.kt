package com.example.baseproject.ui.home.detailchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private var detailMessageRepository: DetailMessageRepository
) : BaseViewModel() {

    private var _sendChatResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendChatResponse: LiveData<Response<Boolean>> get() = _sendChatResponse

    private var messageList = arrayListOf<Chat>()
    private val _messageListLiveData = MutableLiveData<List<Chat>>()
    val messageListLiveData: LiveData<List<Chat>> get() = _messageListLiveData

    fun sendPhoto(chat: Chat, idReceive: String) {
        messageList.add(chat)
        _messageListLiveData.postValue(messageList)
        viewModelScope.launch {
            _sendChatResponse.value = detailMessageRepository.sendPhoto(chat, idReceive)
        }
    }

    fun sendMessage(chat: Chat, idReceive: String) {
        messageList.add(chat)
        _messageListLiveData.postValue(messageList)
        viewModelScope.launch {
            _sendChatResponse.value = detailMessageRepository.sendMessage(chat, idReceive)
        }
    }
}
