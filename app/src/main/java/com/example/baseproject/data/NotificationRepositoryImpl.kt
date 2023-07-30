package com.example.baseproject.data

import androidx.datastore.preferences.SharedPreferencesMigration
import com.example.baseproject.domain.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class NotificationRepositoryImpl : NotificationRepository {
    override fun registerNotificationToken(token: String) {

    }

    override fun unregisterNotificationToken() {

    }
}