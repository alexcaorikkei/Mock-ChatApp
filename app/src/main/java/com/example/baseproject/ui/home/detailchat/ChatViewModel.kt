package com.example.baseproject.ui.home.detailchat

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.baseproject.domain.repository.AuthRepository
import com.example.core.base.BaseViewModel
import com.google.firebase.auth.*
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val savedStateHandle: SavedStateHandle,
    private var detailMessageRepository: DetailMessageRepository,
    private var authRepository: AuthRepository
) : BaseViewModel() {

    private var _sendMessageResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendMessageResponse: LiveData<Response<Boolean>> get() = _sendMessageResponse

    private var _sendPhotoResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendPhotoResponse: LiveData<Response<Boolean>> get() = _sendPhotoResponse

    private var _sendEmojiResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val sendEmojiResponse: LiveData<Response<Boolean>> get() = _sendEmojiResponse

    private var _getReceiverResponse: MutableLiveData<Response<FriendModel>> = MutableLiveData()
    val getReceiverResponse: LiveData<Response<FriendModel>> get() = _getReceiverResponse

    private val _messageListLiveData = MutableLiveData<ArrayList<ChatModel>>()
    val messageListLiveData: LiveData<ArrayList<ChatModel>> get() = _messageListLiveData

    private var receiveData: UserModel? = null
    private val _receiver = MutableLiveData<UserModel>()
    val receiver: LiveData<UserModel> get() = _receiver
    private val myUid = FirebaseAuth.getInstance().currentUser?.uid

    fun getReceiver(idReceive: String) {
        FirebaseDatabase.getInstance().reference.child("users")
            .child(idReceive)
            .child("profile")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    receiveData = UserModel(
                        idReceive,
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
        FirebaseDatabase.getInstance().reference.child("room")
            .child(getIdRoom(myUid.toString(), idReceive))
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(ChatModel::class.java) ?: return
                    val messageList = _messageListLiveData.value ?: arrayListOf()

                    val oldItem = messageList.firstOrNull { it.id == message.id }
                    if (oldItem != null) {
                        oldItem.photo = message.photo
                        _messageListLiveData.value = messageList
                    } else handleAddMessage(message)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    //do nothing
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    //do nothing
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //do nothing
                }

                override fun onCancelled(error: DatabaseError) {
                    //do nothing
                }
            })
    }

    private fun handleAddMessage(message: ChatModel) {
        val messageList = _messageListLiveData.value ?: arrayListOf()

        val mDate = context.getDateChat(message.date.toLong())
        val positionBefore = messageList.size - 1

        if (messageList.isEmpty() || messageList.find { it.currentDate == mDate } == null) {
            messageList.add(ChatModel().apply {
                type = MessageType.DATE
                currentDate = mDate
            })
        }

        if (messageList.size - 1 > 0 && messageList[positionBefore].type != MessageType.DATE) {//tinh ca DATE
            if (messageList[positionBefore].idSender == message.idSender) {
                val compareTwoTime = context.getMinuteSecond(
                    message.date.toLong(),
                    messageList[positionBefore].date.toLong()
                )
                if (compareTwoTime == context.getString(R.string.same_minute))
                    messageList[positionBefore].formatDate = context.getString(
                        R.string.same_minute
                    )
            }
        }
        message.formatDate = context.getMinuteSecond(
            message.date.toLong(),
            0
        )
        if (positionBefore > 0) {
            if (message.formatDate != context.getString(R.string.same_minute)) {
                if (!(messageList[positionBefore].type == MessageType.TEXT && messageList[positionBefore].idSender == myUid)
                    || messageList[positionBefore].formatDate != context.getString(R.string.same_minute)
                ) {
                    message.typeLayout = TypeLayoutChat.ONE
                } else if (messageList[positionBefore].formatDate == context.getString(R.string.same_minute)) {
                    if (message.type == MessageType.TEXT) {
                        if (messageList[positionBefore].typeLayout == TypeLayoutChat.ONE) {
                            messageList[positionBefore].typeLayout = TypeLayoutChat.START
                        } else if (messageList[positionBefore].typeLayout == TypeLayoutChat.END) {
                            messageList[positionBefore].typeLayout = TypeLayoutChat.BETWEEN
                        }
                        message.typeLayout = TypeLayoutChat.END
                    } else if (message.type != MessageType.TEXT) {
                        if (positionBefore > 1) {
                            if (messageList[positionBefore - 1].type == MessageType.TEXT)
                                message.typeLayout = TypeLayoutChat.END
                        } else message.typeLayout = TypeLayoutChat.ONE
                    }
                }

            }
        }
        messageList.add(message)
        _messageListLiveData.value = messageList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendEmoji(linkEmoji: String, idReceive: String) {
        val chatModel = myUid?.let {
            ChatModel(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                it,
                Calendar.getInstance().time.time.toString(),
                MessageType.EMOJI,
                null, null,
                linkEmoji
            )
        }
        if (chatModel != null) {
//            handleAddMessage(chatModel)
            viewModelScope.launch {
                val response = detailMessageRepository.sendMessage(chatModel, idReceive)
                FirebaseDatabase.getInstance().reference
                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .child("notification").apply {
                        child("id").setValue(getIdRoom(FirebaseAuth.getInstance().currentUser?.uid.toString(), idReceive))
                        child("title").setValue(_receiver.value?.displayName)
                        child("emoji").setValue(linkEmoji)
                        child("profile_picture").setValue(_receiver.value?.profilePicture)
                        child("image").setValue(null)
                        child("body").setValue(null)
                    }
                _sendEmojiResponse.postValue(response)
//                sendNotification(
//                    _receiver.value?.displayName, context.getString(R.string.sent_a_emoji)
//                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendPhoto(uri: String, idReceive: String) {
        val chatModel = myUid?.let {
            ChatModel(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                it,
                Calendar.getInstance().time.time.toString(),
                MessageType.PHOTO, null,
                uri,
                null
            )
        }
        if (chatModel != null) {
            handleAddMessage(chatModel)
            viewModelScope.launch {
                val response = detailMessageRepository.sendPhoto(chatModel, idReceive)
                _sendPhotoResponse.value = response
            }
//            sendNotification(
//                _receiver.value?.displayName, context.getString(R.string.sent_a_photo)
//            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(text: String, idReceive: String) {
        val chatModel = myUid?.let {
            ChatModel(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                it,
                Calendar.getInstance().time.time.toString(),
                MessageType.TEXT,
                text
            )
        }
        if (chatModel != null) {
//            handleAddMessage(chatModel)
            viewModelScope.launch {
                val response = detailMessageRepository.sendMessage(chatModel, idReceive)
                _sendMessageResponse.value = response
//                sendNotification(
//                    _receiver.value?.displayName, text
//                )
            }
        }
    }

    fun canOpen() = authRepository.isLogin()

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun sendNotification(displayName: String?, text: String) {
//        displayName?.let { Notification.createNotification(1, context, it, text) }
//    }
}
