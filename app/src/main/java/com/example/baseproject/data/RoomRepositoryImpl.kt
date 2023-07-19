package com.example.baseproject.data

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.R
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.RoomRepository
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.MessageType
import com.example.baseproject.extension.toViWithoutAccent
import com.example.baseproject.ui.home.messages.model.RoomModel
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
import timber.log.Timber
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private const val MILISECONDS_IN_A_DAY = 86400000

class RoomRepositoryImpl : RoomRepository {
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun getMessages(): MutableLiveData<Response<List<RoomModel>>> {
        val roomResponse = MutableLiveData<Response<List<RoomModel>>>()
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

    override suspend fun searchMessages(query: String): Response<List<RoomModel>> {
        if(query.isEmpty()) return Response.Success(listOf())
        return try {
            val roomReference = database.reference.child("room")
            val roomSnapshot = roomReference.get().await()
            val listRoom = getListRoomQuery(roomSnapshot, query.toViWithoutAccent())
            Response.Success(listRoom)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    private suspend fun getListRoomQuery(roomSnapshot: DataSnapshot?, query: String): List<RoomModel> {
        val listRoom = mutableListOf<RoomModel>()
        roomSnapshot?.children?.filter { it.key.toString().contains(auth.uid!!) }?.forEach { room ->
            var messagesMatched = 0
            var textMatched = ""
            var sender = ""
            var time = ""
            room.children.forEach { message ->
                val text = message.child("text").value.toString()
                if (text != "null" && text.toViWithoutAccent().contains(query)) {
                    messagesMatched += 1
                    textMatched = text
                    sender = message.child("idSender").value.toString()
                    time = message.child("date").value.toString()
                }
            }
            if(messagesMatched > 0) {
                val id = room.key.toString()
                val friendId = id.replace(auth.uid!!, "")
                val friendProfile = getFriendProfile(friendId)
                listRoom.add(
                    RoomModel(
                        id,
                        friendId,
                        friendProfile.displayName,
                        friendProfile.profilePicture,
                        MessageType.TEXT,
                        textMatched,
                        time,
                        isSent = sender == auth.uid!!,
                        messagesMatched = messagesMatched,
                    )
                )
            }
        }
        return listRoom.sortedBy { -it.time.toLong() }.toList()
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
                lastMessage.child("idSender").value.toString(),
                lastMessage.child("date").value.toString(),
                type,
                lastMessage.child(type.reference).value.toString(),
            )
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getDate(date: Long): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val hourFormat: DateFormat = SimpleDateFormat("HH:mm")
        val today = Calendar.getInstance()
        today.set(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH),
            0, 0, 0)
        val messageDate = Date(date)
        return if(dateFormat.format(today.time) == dateFormat.format(messageDate)) {
            hourFormat.format(messageDate)
        } else if(today.time.time - messageDate.time < MILISECONDS_IN_A_DAY) {
            R.string.yesterday.toString()
        } else {
            dateFormat.format(messageDate)
        }

    }

    private suspend fun getListRoom(snapshot: DataSnapshot): List<RoomModel>{
        val listRoom = mutableListOf<RoomModel>()
        for (room in snapshot.children) {
            val id = room.key.toString()
            if(id.contains(auth.uid!!)) {
                val friendId = id.replace(auth.uid!!, "")
                val friendProfile = getFriendProfile(friendId)
                val lastMessage = getLastMessage(room.key.toString())
                listRoom.add(
                    RoomModel(
                        room.child("id").value.toString(),
                        friendId,
                        friendProfile.displayName,
                        friendProfile.profilePicture,
                        lastMessage.type,
                        lastMessage.text!!,
                        lastMessage.date,
                        false,
                        lastMessage.idSender == auth.uid!!
                    )
                )
            }
        }
        return listRoom.sortedBy {
            -it.time.toLong()
        }.map{
            it.copy(time = getDate(it.time.toLong()))
        }.toList()
    }
}