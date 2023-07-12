package com.example.baseproject.ui.home.detailchat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.baseproject.extension.*
import com.example.core.base.BaseViewModel
import com.google.firebase.auth.*
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private var detailMessageRepository: DetailMessageRepository
) : BaseViewModel() {

    private var _sendChatResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendChatResponse: LiveData<Response<Boolean>> get() = _sendChatResponse

    private var messageList = arrayListOf<Chat>()
    private val _messageListLiveData = MutableLiveData<List<Chat>>()
    val messageListLiveData: LiveData<List<Chat>> get() = _messageListLiveData

    val uid = MutableLiveData<String>()

    init {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            uid.value = it.uid
        }
    }

    fun getListMessage() {
        val user = FirebaseAuth.getInstance().currentUser
        val idSender = user?.uid
        FirebaseDatabase.getInstance().reference.child("room")
            .child(getIdRoom(idSender.toString(), ID_RECEIVE_N))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageListFirebase = ArrayList<Chat>()
                    for (dataSnapshot in snapshot.children) {
                        val message = dataSnapshot.getValue(Chat::class.java)
                        if (message != null) {
                            messageListFirebase.add(message)
                            Log.d("VMCHAT", "onDataChange: $message")
                        }
                    }
                    _messageListLiveData.value = messageListFirebase
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun sendPhoto(chat: Chat, idReceive: String) {
        messageList.add(chat)
        _messageListLiveData.postValue(messageList)
        viewModelScope.launch {
            _sendChatResponse.value = detailMessageRepository.sendPhoto(chat, idReceive)
        }
    }

    fun sendMessage(chat: Chat, idReceive: String) {
        messageList.add(chat)
        _messageListLiveData.postValue(messageList)
        viewModelScope.launch {
            _sendChatResponse.value = detailMessageRepository.sendMessage(chat, idReceive)
        }
    }
}
