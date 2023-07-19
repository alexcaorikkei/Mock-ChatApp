package com.example.baseproject.data

import androidx.core.net.toUri
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.baseproject.extension.*
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.FriendModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await


class DetailMessageRepositoryImpl : DetailMessageRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override suspend fun sendMessage(
        chatModel: ChatModel,
        idReceive: String
    ): Response<Boolean> {
        database.reference.child("room")
            .child(getIdRoom(auth.currentUser?.uid.toString(), idReceive))
            .child(chatModel.id).setValue(chatModel)

        return try {
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendPhoto(
        chatModel: ChatModel,
        idReceive: String
    ): Response<Boolean> {
        chatModel.photo?.let {
            storage.reference.child("images/" + it.toUri().lastPathSegment)
                .putFile(it.toUri())
                .addOnSuccessListener { task ->
                    task.storage.downloadUrl.addOnSuccessListener { uri ->
                        chatModel.photo = uri.toString()
                        database.reference.child("room")
                            .child(getIdRoom(auth.currentUser?.uid.toString(), idReceive))
                            .child(chatModel.id).setValue(chatModel)
                    }
                }
                .addOnFailureListener { it2 ->
                    print(it2.message)
                }
        }

        return try {
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getInformationReceiver(idReceive: String): Response<FriendModel> {
        return try {
            val friend =
                database.reference.child("users").child(idReceive).child("profile").get().await()
            Response.Success(
                FriendModel(
                    friend.key.toString(),
                    friend.child("display_name").value.toString(),
                    friend.child("profile_picture").value.toString(),
                )
            )
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getListMessage(idReceive: String): Response<List<ChatModel>> {
        return try {
            val messageListFirebase = mutableListOf<ChatModel>()

            auth.uid?.let {
                database.reference.child("room")
                    .child(getIdRoom(it, idReceive))
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                val message = dataSnapshot.getValue(ChatModel::class.java)
                                if (message != null) {
                                    messageListFirebase.add(message)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
            Response.Success(messageListFirebase)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}