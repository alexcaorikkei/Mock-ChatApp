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
    var currentQuery = ""
    private val _searchResponse = MutableLiveData<Response<List<FriendModel>>>()
    val searchResponse: LiveData<Response<List<FriendModel>>> = _searchResponse

    fun searchAllUserWithCurrentAccount(query: String) {
        currentQuery = query
        viewModelScope.launch {
            _searchResponse.value = Response.Loading
            _searchResponse.value = friendRepository.searchAllUserWithCurrentAccount(query.toViWithoutAccent())
        }
    }

    private val _friendChangeStateResponse = MutableLiveData<Response<String>>()
    val friendChangeStateResponse: LiveData<Response<String>> = _friendChangeStateResponse

    fun acceptFriend(uid: String) {
        viewModelScope.launch {
            _friendChangeStateResponse.value = Response.Loading
            _friendChangeStateResponse.value = friendRepository.acceptFriendRequest(uid)
            _searchResponse.value = Response.Success(
                getNewListFriends(
                    (_searchResponse.value as Response.Success<List<FriendModel>>).data,
                    uid,
                    FriendState.FRIEND
                )
            )
        }
    }

    fun addFriend(uid: String) {
        viewModelScope.launch {
            _friendChangeStateResponse.value = Response.Loading
            _friendChangeStateResponse.value = friendRepository.sendFriendRequest(uid)
            _searchResponse.value = Response.Success(
                getNewListFriends(
                    (_searchResponse.value as Response.Success<List<FriendModel>>).data,
                    uid,
                    FriendState.ADDED
                )
            )
        }
    }

    fun cancelFriend(uid: String) {
        viewModelScope.launch {
            _friendChangeStateResponse.value = Response.Loading
            _friendChangeStateResponse.value = friendRepository.cancelFriendRequest(uid)
            _searchResponse.value = Response.Success(
                getNewListFriends(
                    (_searchResponse.value as Response.Success<List<FriendModel>>).data,
                    uid,
                    FriendState.NONE
                )
            )
        }
    }

    private fun getNewListFriends(oldListFriends: List<FriendModel>, uid: String, state: FriendState): List<FriendModel> {
        return oldListFriends.map { friend ->
            if (friend.uid == uid) {
                friend.copy(state = state)
            } else {
                friend
            }
        }
    }
}