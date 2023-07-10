package com.example.baseproject.ui.home.detailchat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.core.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

//    fun getListMessage() {
//        FirebaseDatabase.getInstance().reference.child("room")
////                .child( FirebaseAuth.getInstance().currentUser?.uid + "OVC9HAzZmFPmHrfYi7IZNExg8Us2")
//
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val messageListFirebase = ArrayList<Chat>()
//                    for (dataSnapshot in snapshot.children) {
//                        val message = dataSnapshot.getValue(Chat::class.java)
//                        if (message != null) {
//                            messageListFirebase.add(message)
//                        }
//                    }
//                    _messageListLiveData.postValue(messageListFirebase)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//        val user = Firebase.auth.currentUser
//        user?.let {
//            Log.d("id_Sender", it.uid)
//            room.id?.let { it1 ->
//                FirebaseDatabase.getInstance().reference.child("users").child(it.uid)
//                    .child("profile").setValue(room)
//            }
//        }
//    }

//    fun getDataSender() {
//        FirebaseDatabase.getInstance().reference.child("users")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val user = Firebase.auth.currentUser
//                    user?.uid?.let {
//                        FirebaseDatabase.getInstance().reference.child(it)
//                            .addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    sender = snapshot.getValue(User::class.java)
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                }
//
//                            }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })
//    }


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
