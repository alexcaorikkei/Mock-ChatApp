package com.example.baseproject.ui.home.messages

import androidx.lifecycle.SavedStateHandle
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    val savedStateHandle : SavedStateHandle
) : BaseViewModel() {

}