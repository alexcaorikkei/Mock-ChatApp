package com.example.baseproject.ui.home.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.AuthRepository
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val authRepository: AuthRepository,
) : BaseViewModel() {
    private val _searchAllUserResponse = MutableLiveData<Response<List<FriendModel>>>()
    val searchAllUserResponse: LiveData<Response<List<FriendModel>>> = _searchAllUserResponse

    fun searchAllUser(query: String) {
        viewModelScope.launch {
            _searchAllUserResponse.value = Response.Loading
//            _searchAllUserResponse.value = realTimeRepository.searchAllUser(authRepository.currentUser!!.uid,query)
        }
    }

    private val _searchFriendsResponse = MutableLiveData<Response<List<FriendModel>>>()
    val searchFriendsResponse: LiveData<Response<List<FriendModel>>> = _searchFriendsResponse

    fun searchFriends(query: String) {
        viewModelScope.launch {
            _searchFriendsResponse.value = Response.Loading
//            _searchFriendsResponse.value = realTimeRepository.searchFriends(authRepository.currentUser!!.uid,query)
        }
    }

    private val _searchRequestResponse = MutableLiveData<Response<List<FriendModel>>>()
    val searchRequestResponse: LiveData<Response<List<FriendModel>>> = _searchRequestResponse

    fun searchRequest(query: String) {
        viewModelScope.launch {
            _searchAllUserResponse.value = Response.Loading
//            _searchAllUserResponse.value = realTimeRepository.searchRequest(authRepository.currentUser!!.uid,query)
            Timber.d("searchForFriend: ${_searchAllUserResponse.value}")
        }
    }
}