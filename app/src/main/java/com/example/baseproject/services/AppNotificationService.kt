package com.example.baseproject.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.baseproject.R
import com.example.baseproject.container.MainActivity
import com.example.baseproject.domain.model.NotificationModel
import com.example.baseproject.domain.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.net.URL
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
            sendNotification(it)
        })
        Timber.d("Notification service started")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendNotification(notification: NotificationModel) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val newMessageNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(notification.title)
            .setContentText(
                if (notification.body != null)
                    notification.body
                else
                    "${notification.title} ${getString(R.string.sent_a_photo)}"
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


