package com.example.baseproject.ui.home.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.FriendRepository
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val friendRepository: FriendRepository
) : BaseViewModel() {
    private val _searchResponse = MutableLiveData<Response<List<FriendModel>>>()
    val searchResponse: LiveData<Response<List<FriendModel>>> = _searchResponse

    fun searchAllUserWithCurrentAccount(query: String) {
        viewModelScope.launch {
            _searchResponse.value = Response.Loading
            _searchResponse.value = friendRepository.searchAllUserWithCurrentAccount(query)
        }
    }

    private val _friendChangeStateResponse = MutableLiveData<Response<String>>()
    val friendChangeStateResponse: LiveData<Response<String>> = _friendChangeStateResponse

    fun acceptFriend(uid: String) {
        viewModelScope.launch {
            _friendChangeStateResponse.value = Response.Loading
            _friendChangeStateResponse.value = friendRepository.acceptFriendRequest(uid)
        }
    }

    fun addFriend(uid: String) {
        viewModelScope.launch {
            _friendChangeStateResponse.value = Response.Loading
            _friendChangeStateResponse.value = friendRepository.sendFriendRequest(uid)
        }
    }

    fun cancelFriend(uid: String) {
        viewModelScope.launch {
            _friendChangeStateResponse.value = Response.Loading
            _friendChangeStateResponse.value = friendRepository.cancelFriendRequest(uid)
        }
    }
}