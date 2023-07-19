package com.example.baseproject.ui.home.detailchat.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("token", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message != null) {
            Notification.createNotification(
                2,
                this,
                message.notification!!.title!!,
                message.notification!!.body!!
            )
        }
    }
}