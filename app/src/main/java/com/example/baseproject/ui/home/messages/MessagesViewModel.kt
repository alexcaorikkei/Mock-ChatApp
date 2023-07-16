package com.example.baseproject.ui.home.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle
) : BaseViewModel() {
    private val _searchResponse = MutableLiveData<Response<List<MessageModel>>>()
    val searchResponse: LiveData<Response<List<MessageModel>>> = _searchResponse

    fun searchMessages(query: String) {
        viewModelScope.launch {
            _searchResponse.value = Response.Loading
            _searchResponse.value = Response.Success(listOf(
                MessageModel(1, "Nguyen Van A", "Hello", "10:00", "", false),
                MessageModel(2, "Nguyen Van B", "Hello", "10:00", "", true),
                MessageModel(3, "Nguyen Van C", "Hello", "10:00", "", true),
                MessageModel(4, "Nguyen Van D", "Hello", "10:00", "", false),
                MessageModel(5, "Nguyen Van E", "Hello", "10:00", "", false),
                MessageModel(6, "Nguyen Van F", "Hello", "10:00", "", true),
                MessageModel(7, "Nguyen Van G", "Hello", "10:00", "", false),
                MessageModel(8, "Nguyen Van H", "Hello", "10:00", "", true),
                MessageModel(9, "Nguyen Van I", "Hello", "10:00", "", false),
                MessageModel(10, "Nguyen Van J", "Hello", "10:00", "", true),
                MessageModel(11, "Nguyen Van K", "Hello", "10:00", "", false),
                MessageModel(12, "Nguyen Van L", "Hello", "10:00", "", true),
                MessageModel(7, "Nguyen Van G", "Hello", "10:00", "", false),
                MessageModel(8, "Nguyen Van H", "Hello", "10:00", "", true),
                MessageModel(9, "Nguyen Van I", "Hello", "10:00", "", false),
                MessageModel(10, "Nguyen Van J", "Hello", "10:00", "", true),
                MessageModel(11, "Nguyen Van K", "Hello", "10:00", "", false),
                MessageModel(12, "Nguyen Van L", "Hello", "10:00", "", true),
            ))
        }
    }
}