package com.example.baseproject.data

import androidx.core.net.toUri
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.baseproject.extension.*
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException


class DetailMessageRepositoryImpl : DetailMessageRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override suspend fun sendMessage(
        chatModel: ChatModel,
        idReceive: String
    ): Response<Boolean> {
        return try {
            database.reference.child("room")
                .child(getIdRoom(auth.currentUser?.uid.toString(), idReceive))
                .child(chatModel.id).setValue(chatModel)

            val profile = database.reference.child("users")
                .child(auth.currentUser?.uid.toString())
                .child("profile").get().await()

            database.reference.child("users")
                .child(idReceive)
                .child("notification").setValue(
                    NotificationModel(
                        profile.child("display_name").value.toString(),
                        body = chatModel.text,
                        profilePicture = profile.child("profile_picture").value.toString()
                    )
                )
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun sendPhoto(
        chatModel: ChatModel,
        idReceive: String
    ): Response<Boolean>  {
        val profile = database.reference.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("profile").get().await()
        return suspendCancellableCoroutine { emit ->
            chatModel.photo?.let {
                storage.reference
                    .child("images/" + it.toUri().lastPathSegment)
                    .putFile(it.toUri())
                    .addOnSuccessListener { task ->
                        task.storage
                            .downloadUrl
                            .addOnSuccessListener { uri ->
                                chatModel.photo = uri.toString()
                                database.reference
                                    .child("room")
                                    .child(getIdRoom(auth.currentUser?.uid.toString(), idReceive))
                                    .child(chatModel.id).setValue(chatModel)
                                    .addOnSuccessListener {
                                        emit.resume(Response.Success(true), null)
                                    }.addOnFailureListener { e ->
                                        emit.resumeWithException(e)
                                    }
                                database.reference.child("users")
                                    .child(idReceive)
                                    .child("notification").setValue(
                                        NotificationModel(
                                            profile.child("display_name").value.toString(),
                                            image = chatModel.photo,
                                            profilePicture = profile.child("profile_picture").value.toString()
                                        )
                                    )
                            }.addOnFailureListener { e ->
                                emit.resumeWithException(e)
                            }
                    }.addOnFailureListener { it2 ->
                        emit.resumeWithException(it2)
                    }
            }
        }
    }

}