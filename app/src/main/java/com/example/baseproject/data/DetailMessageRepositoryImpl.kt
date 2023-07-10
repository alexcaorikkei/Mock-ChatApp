package com.example.baseproject.data

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.DetailMessageRepository
import com.example.baseproject.extension.*
import com.example.baseproject.ui.home.detailchat.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class DetailMessageRepositoryImpl : DetailMessageRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override suspend fun sendMessage(
        chat: Chat,
        idReceive: String
    ): Response<Boolean> {
        database.reference.child("room")
            .child(auth.currentUser?.uid + idReceive)
            .child(chat.id).setValue(chat)

        return try {
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendPhoto(
        chat: Chat,
        idReceive: String
    ): Response<Boolean> {
        chat.photoList?.forEach {
            storage.reference.child("images/" + it.lastPathSegment)
                .putFile(it)
                .addOnSuccessListener { task ->
                    task.storage.downloadUrl.addOnSuccessListener { uri ->
                        val myChat = Chat(
                            database.reference.push().key.toString(),
                            null,
                            SEND_PHOTOS,
                            null,
                            getTimeCurrent(),
                            uri.toString(),
                            null
                        )
                        database.reference.child("room")
                            .child(auth.currentUser?.uid + idReceive)
                            .child(chat.id).setValue(myChat)
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
}