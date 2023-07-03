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
class RegisterViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle,
    private val authRepository: AuthRepository,
) :BaseViewModel() {
    private var _signUpResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val signUpResponse: LiveData<Response<Boolean>> get() = _signUpResponse

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _signUpResponse.value = Response.Loading
            _signUpResponse.value = authRepository.firebaseSignUp(email, password, displayName)
        }
    }


    private var _validator: MutableLiveData<Boolean> = MutableLiveData()
    val validator: LiveData<Boolean> get() = _validator
    private var _isValidEmail = false
    private var _isValidPassword = false
    private var _isValidDisplayName = false
    private var _isChecked = false
    fun setValidState(
        isValidEmail: Boolean? = _isValidEmail,
        isValidPassword: Boolean? = _isValidPassword,
        isValidDisplayName: Boolean? = _isValidDisplayName,
        isChecked: Boolean? = _isChecked) {
        _isValidEmail = isValidEmail!!
        _isValidPassword = isValidPassword!!
        _isValidDisplayName = isValidDisplayName!!
        _isChecked = isChecked!!
        _validator.value = _isValidEmail && _isValidPassword && _isValidDisplayName && _isChecked
    }
}