package com.example.baseproject.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.AuthRepository
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val authRepository: AuthRepository
) :BaseViewModel() {
    private var _signInResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val signInResponse: LiveData<Response<Boolean>> get() = _signInResponse

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInResponse.value = Response.Loading
            _signInResponse.value = authRepository.firebaseLogin(email, password)
        }
    }
}