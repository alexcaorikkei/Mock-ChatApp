package com.example.baseproject.ui.home.detailchat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.baseproject.R
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.baseproject.extension.*
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.domain.model.UserModel
import com.example.core.base.BaseViewModel
import com.example.core.utils.SingleLiveEvent
import com.google.firebase.auth.*
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private var detailMessageRepository: DetailMessageRepository
) : BaseViewModel() {

    private var _sendMessageResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendMessageResponse: LiveData<Response<Boolean>> get() = _sendMessageResponse

    private var _sendPhotoResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendPhotoResponse: LiveData<Response<Boolean>> get() = _sendPhotoResponse

    private var _sendEmojiResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendEmojiResponse: LiveData<Response<Boolean>> get() = _sendEmojiResponse

    private var _getReceiverResponse: MutableLiveData<Response<FriendModel>> = MutableLiveData()
    val getReceiverResponse: LiveData<Response<FriendModel>> get() = _getReceiverResponse

    private var _getListChatResponseModel: MutableLiveData<Response<List<ChatModel>>> =
        MutableLiveData()
    val getListChatResponseModel: LiveData<Response<List<ChatModel>>> get() = _getListChatResponseModel


    private var messageList = arrayListOf<ChatModel>()
    private val _messageListLiveData = MutableLiveData<ArrayList<ChatModel>>()
    val messageListLiveData: LiveData<ArrayList<ChatModel>> get() = _messageListLiveData

    private var receiveData: UserModel? = null
    private val _receiver = MutableLiveData<UserModel>()
    val receiver: LiveData<UserModel> get() = _receiver

    val uid = SingleLiveEvent<String>()

    init {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            uid.value = it.uid
        }
    }

    fun getReceiver(idReceive: String) {
//        viewModelScope.launch {
//            _getReceiverResponse.value =
//                detailMessageRepository.getInformationReceiver(KEY_ID_RECEIVER)
//            if (_getReceiverResponse.value is Response.Success) {
//                receiveData = (_getReceiverResponse.value as Response.Success).data
//            }
//        }
        FirebaseDatabase.getInstance().reference.child("users")
            .child(idReceive)
            .child("profile")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    receiveData = UserModel(
                        "",
                        snapshot.child("display_name").value.toString(),
                        snapshot.child("phone_number").value.toString(),
                        snapshot.child("birthday").value.toString(),
                        snapshot.child("email").value.toString(),
                        snapshot.child("profile_picture").value.toString()
                    )
                    receiveData?.let {
                        _receiver.value = it
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getListMessage(idReceive: String) {
//        viewModelScope.launch {
//            _getListChatResponse.value = detailMessageRepository.getListMessage(idReceive)
//            if (_getListChatResponse.value is Response.Success) {
//                messageList.addAll((_getListChatResponse.value as Response.Success).data)
//                Log.d("ngocc", "getListMessage: $messageList")
//            }
//        }

        val user = FirebaseAuth.getInstance().currentUser
        val idSender = user?.uid
        FirebaseDatabase.getInstance().reference.child("room")
            .child(getIdRoom(idSender.toString(), idReceive))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageListFirebase = ArrayList<ChatModel>()
                    for (dataSnapshot in snapshot.children) {
                        val message = dataSnapshot.getValue(ChatModel::class.java)
                        if (message != null) {
                            messageListFirebase.add(message)
                        }
                    }
                    _messageListLiveData.value = messageListFirebase
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun sendEmoji(linkEmoji: String, idReceive: String) {
        val chatModel = uid.value?.let {
            ChatModel(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                it,
                getTimeCurrent(),
                MessageType.EMOJI,
                null, null,
                linkEmoji
            )
        }
        if (chatModel != null) {
            viewModelScope.launch {
                val response = detailMessageRepository.sendMessage(chatModel, idReceive)
                _sendEmojiResponse.value = response
                if (response is Response.Success) {
                    messageList.add(chatModel)
                    _messageListLiveData.postValue(messageList)
                }
            }
        }
    }


    fun sendPhoto(uri: String, idReceive: String) {
        val chatModel = uid.value?.let {
            ChatModel(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                it,
                getTimeCurrent(),
                MessageType.PHOTO, null,
                uri,
                null
            )
        }
        if (chatModel != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = detailMessageRepository.sendPhoto(chatModel, idReceive)

                _sendPhotoResponse.postValue(response)
                if (response is Response.Success) {
                    messageList.add(chatModel)
                    _messageListLiveData.postValue(messageList)
                }
            }
        }
    }

    fun sendMessage(text: String, idReceive: String) {
        val chatModel = uid.value?.let {
            ChatModel(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                it,
                getTimeCurrent(),
                MessageType.TEXT,
                text
            )
        }
        if (chatModel != null) {

            viewModelScope.launch {
                val response = detailMessageRepository.sendMessage(chatModel, idReceive)
                _sendMessageResponse.value = response
                if (response is Response.Success) {
                    messageList.add(chatModel)
                    _messageListLiveData.postValue(messageList)
                }
            }
        }
    }
}
