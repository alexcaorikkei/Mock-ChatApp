package com.example.baseproject.data

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.MessageRepository
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.ui.home.messages.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date


class MessageRepositoryImpl : MessageRepository {
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun getMessages(): MutableLiveData<Response<List<MessageModel>>> {
        val roomResponse = MutableLiveData<Response<List<MessageModel>>>()
        roomResponse.postValue(Response.Loading)
        val roomReference = database.reference.child("room")
        val roomListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                coroutineScope.launch {
                    roomResponse.postValue(Response.Success(getListRoom(snapshot)))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                roomResponse.postValue(Response.Failure(error.toException()))
            }

        }
        roomReference.addValueEventListener(roomListener)
        return roomResponse
    }

    private suspend fun getFriendProfile(friendId: String): FriendModel {
        return try {
            val friendProfile = database.reference.child("users").child(friendId).child("profile").get().await()
            FriendModel(
                friendId,
                friendProfile.child("display_name").value.toString(),
                friendProfile.child("profile_picture").value.toString(),
            )
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getLastMessage(roomId: String): ChatModel {
        return try {
            val lastMessage = database.reference.child("room").child(roomId).get().await().children.last()
            val type = MessageType.fromString(lastMessage.child("type").value.toString())
            ChatModel(
                lastMessage.key.toString(),
                lastMessage.key.toString().replace(auth.uid!!, ""),
                getDate(lastMessage.child("date").value.toString()),
                type,
                lastMessage.child(type.reference).value.toString(),
            )
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getDate(date: String): String {
        val formatFull = SimpleDateFormat("EE, dd-MM-yyyy:HH:mm")
        val formatDay = SimpleDateFormat("dd-MM-yyyy")
        val formatHour = SimpleDateFormat("HH:mm")
        val messageDate = formatFull.parse(date)
        val currentDate = Date()
        return if(formatDay.format(messageDate!!) == formatDay.format(currentDate)) {
            formatHour.format(messageDate)
        } else {
            formatDay.format(messageDate)
        }
    }

    private suspend fun getListRoom(snapshot: DataSnapshot): List<MessageModel>{
        val listRoom = mutableListOf<MessageModel>()
        for (room in snapshot.children) {
            val id = room.key.toString()
            if(id.contains(auth.uid!!)) {
                val friendId = id.replace(auth.uid!!, "")
                val friendProfile = getFriendProfile(friendId)
                val lastMessage = getLastMessage(room.key.toString())
                listRoom.add(
                    MessageModel(
                        room.child("id").value.toString(),
                        friendProfile.displayName,
                        friendProfile.profilePicture,
                        lastMessage.type,
                        lastMessage.text!!,
                        lastMessage.date,
                        false
                    )
                )
            }
        }
        return listRoom.toList()
    }
}