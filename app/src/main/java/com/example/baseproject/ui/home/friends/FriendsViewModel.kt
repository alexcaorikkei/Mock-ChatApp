package com.example.baseproject.ui.home.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.FriendRepository
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.extension.toViWithoutAccent
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val friendRepository: FriendRepository
) : BaseViewModel() {
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

    private val _listFriendLiveData = friendRepository.getFriends()
    val listFriend: LiveData<Response<List<FriendModel>>> = _listFriendLiveData
    private val _queryData = MutableLiveData("")
    val queryData: LiveData<String> = _queryData

    fun searchFriend(query: String) {
        _queryData.postValue(query)
    }
}