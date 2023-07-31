package com.example.baseproject.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.navigation.NavDeepLinkBuilder
import com.example.baseproject.R
import com.example.baseproject.container.MainActivity
import com.example.baseproject.domain.model.NotificationModel
import com.example.baseproject.domain.repository.NotificationRepository
import com.example.baseproject.extension.KEY_ID_RECEIVER
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AppNotificationService : LifecycleService() {

    companion object {
        const val CHANNEL_ID = "com.example.baseproject.CHANNEL_ID"
        const val NAME = "Base Project"
    }

    @Inject
    lateinit var notificationRepository: NotificationRepository


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationRepository.getNotification().observe(this, Observer {
            if(it?.id != null) {
                Timber.d("Notification received: ${it.id}")
                sendNotification(it)
            }
        })
        Timber.d("Notification service started")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendNotification(notification: NotificationModel) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_navigation)
            .setDestination(R.id.chatFragment)
            .setArguments(
                Bundle().apply {
                    putString(KEY_ID_RECEIVER, notification.id)
                }
            )
            .createPendingIntent()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val newMessageNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(notification.title)
            .setContentText(
                if (notification.body != null)
                    notification.body
                else if(notification.image != null)
                    "${notification.title} ${getString(R.string.sent_a_photo)}"
                else
                    "${notification.title} ${getString(R.string.sent_an_emoji)}"
            )
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_user_id
                )
            )
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        newMessageNotification.build()
        notificationManager.notify(0, newMessageNotification.build())
    }

}


