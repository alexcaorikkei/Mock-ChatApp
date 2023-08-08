package com.example.baseproject.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.navigation.NavDeepLinkBuilder
import com.example.baseproject.R
import com.example.baseproject.container.MainActivity
import com.example.baseproject.domain.model.NotificationModel
import com.example.baseproject.domain.repository.NotificationRepository
import com.example.baseproject.extension.KEY_ID_RECEIVER
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@AndroidEntryPoint
class AppNotificationService : LifecycleService() {

    companion object {
        const val CHANNEL_ID = "com.example.baseproject.hmm..."
    }

    @Inject
    lateinit var notificationRepository: NotificationRepository


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        notificationRepository.getNotification().observe(this) {
            if (it?.id != null) {
                Timber.d("Notification received: ${it.id}")
                sendNotification(it)
            }
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                Timber.d("Notification service running")
            }
        }, 10000, 1000)
        Timber.d("Notification service started")
        return START_STICKY
    }

    override fun onDestroy() {
        Timber.d("Notification service destroyed")
        super.onDestroy()
    }

    private fun sendNotification(notification: NotificationModel) {
        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager=
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
