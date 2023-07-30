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
    private val profileRepository: ProfileRepository,
) : BaseViewModel() {
    private var _logOutResponse = SingleLiveEvent<Response<Boolean>>()
    val logOutResponse: MutableLiveData<Response<Boolean>> = _logOutResponse
    fun logOut() {
        viewModelScope.launch {
            _logOutResponse.value = Response.Loading
            _logOutResponse.value = authRepository.logOut()
        }
    }

    private var _profileResponse = profileRepository.getProfile()
    val profileResponse: LiveData<Response<UserModel>> = _profileResponse

    fun updateProfile(user: UserModel, profilePictureUri: Uri?) {
        viewModelScope.launch {
            _editResponse.postValue(Response.Loading)
            _editResponse.postValue(profileRepository.updateProfile(user, profilePictureUri))
        }
    }

    private var _editResponse = MutableLiveData<Response<Boolean>>()
    val editResponse: MutableLiveData<Response<Boolean>> get() = _editResponse

    private var _validator: MutableLiveData<Boolean> = MutableLiveData()
    val validator: LiveData<Boolean> get() = _validator
    private var _isValidPhone = false
    private var _isValidName = false
    fun setValidState(
        isValidPhone: Boolean? = _isValidPhone,
        isValidName: Boolean? = _isValidName) {
        _isValidPhone = isValidPhone!!
        _isValidName = isValidName!!
        _validator.value = _isValidPhone && _isValidName
    }
}
