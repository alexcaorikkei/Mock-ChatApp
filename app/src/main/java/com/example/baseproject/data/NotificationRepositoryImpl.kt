package com.example.baseproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.NotificationModel
import com.example.baseproject.domain.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber
import java.io.FileInputStream

class NotificationRepositoryImpl : NotificationRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun getNotification(): LiveData<NotificationModel> {
        val notificationLiveData = MutableLiveData<NotificationModel>()
        database.reference.child("users")
            .child(auth.currentUser!!.uid)
            .child("notification")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val notification = NotificationModel(
                        title = snapshot.child("title").value?.toString(),
                        id = snapshot.child("id").value?.toString()?.replace(auth.currentUser!!.uid, ""),
                        body = snapshot.child("body").value?.toString(),
                        image = snapshot.child("image").value?.toString(),
                        emoji = snapshot.child("emoji").value?.toString(),
                        profilePicture = snapshot.child("profile_picture").value?.toString()
                    )
                    notificationLiveData.postValue(notification)
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Timber.e(error.toException())
                }
            })
        return notificationLiveData
    }

    override fun canOpenNotification(): Boolean {
        return auth.currentUser != null
    }
}