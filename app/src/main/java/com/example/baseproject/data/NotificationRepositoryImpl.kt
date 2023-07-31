package com.example.baseproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.repository.NotificationRepository
import com.example.baseproject.domain.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

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
                    val notification = snapshot.getValue(NotificationModel::class.java)
                    notification?.let { notificationLiveData.postValue(it) }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Timber.e(error.toException())
                }
            })
        return notificationLiveData
    }

}