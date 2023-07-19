package com.example.baseproject.ui.home.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.AuthRepository
import com.example.baseproject.domain.repository.ProfileRepository
import com.example.baseproject.domain.model.UserModel
import com.example.core.base.BaseViewModel
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {
    private var _logOutResponse = SingleLiveEvent<Response<Boolean>>()
    val logOutResponse: MutableLiveData<Response<Boolean>> = _logOutResponse
    fun logOut() {
        viewModelScope.launch {
            _logOutResponse.value = Response.Loading
            _logOutResponse.value = authRepository.logOut()
        }
    }

    private var _profileResponse = MutableLiveData<Response<UserModel>>()
    val profileResponse: LiveData<Response<UserModel>> = _profileResponse
    fun getProfile() {
        viewModelScope.launch {
            _profileResponse.value = Response.Loading
            _profileResponse.value = profileRepository.getProfile()
        }
    }

    fun updateProfile(user: UserModel, profilePictureUri: Uri?) {
        viewModelScope.launch {
            _profileResponse.value = Response.Loading
            profileRepository.updateProfile(user, profilePictureUri)
            _profileResponse.value = profileRepository.getProfile()
        }
    }
}
