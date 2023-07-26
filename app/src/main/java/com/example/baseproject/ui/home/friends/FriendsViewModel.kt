package com.example.baseproject.ui.home.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.FriendRepository
import com.example.baseproject.domain.model.FriendModel
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val friendRepository: FriendRepository
) : BaseViewModel() {
    private var _listFriendLiveData = friendRepository.getFriends()
    val listFriendLiveData: LiveData<Response<List<FriendModel>>> = _listFriendLiveData

    private var _queryResponse = MutableLiveData("")
    val queryResponse: LiveData<String> = _queryResponse

    val friendStateResponse = MutableLiveData<Response<Boolean>>()

    fun acceptFriend(uid: String) {
        viewModelScope.launch {
            friendStateResponse.postValue(Response.Loading)
            val response = friendRepository.acceptFriend(uid)
            friendStateResponse.postValue(response)
        }
    }

    fun cancelFriend(uid: String) {
        viewModelScope.launch {
            friendStateResponse.postValue(Response.Loading)
            val response = friendRepository.cancelFriend(uid)
            friendStateResponse.postValue(response)
        }
    }

    fun addFriend(uid: String) {
        viewModelScope.launch {
            friendStateResponse.postValue(Response.Loading)
            val response = friendRepository.addFriend(uid)
            friendStateResponse.postValue(response)
        }
    }

    fun searchFriend(query: String) {
        _queryResponse.postValue(query)
    }
}