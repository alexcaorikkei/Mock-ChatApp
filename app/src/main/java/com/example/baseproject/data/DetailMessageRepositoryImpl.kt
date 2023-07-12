package com.example.baseproject.data

import androidx.core.net.toUri
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
            .child(getIdRoom(auth.currentUser?.uid.toString(), idReceive))
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
        chat.description?.let {
            storage.reference.child("images/" + it.toUri().lastPathSegment)
                .putFile(it.toUri())
                .addOnSuccessListener { task ->
                    task.storage.downloadUrl.addOnSuccessListener { uri ->
                        val myChat = auth.uid?.let { it1 ->
                            Chat(
                                database.reference.push().key.toString(),
                                it1,
                                getTimeCurrent(),
                                uri.toString()
                            )
                        }
                        database.reference.child("room")
                            .child(getIdRoom(auth.currentUser?.uid.toString(), idReceive))
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