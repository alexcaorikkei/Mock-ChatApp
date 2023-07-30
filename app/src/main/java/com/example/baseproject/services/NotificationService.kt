package com.example.baseproject.services

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.baseproject.container.MainActivity
import com.example.baseproject.data.NotificationRepositoryImpl
import com.example.baseproject.domain.repository.NotificationRepository
import com.example.core.pref.RxPreferences
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var rxPreferences: RxPreferences

    private val scope = CoroutineScope(Dispatchers.IO)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch {
            rxPreferences.saveNotificationToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            sendNotification(message.notification!!.body!!)
        }
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId ="R.string.project_id"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            channelId
        )
            .setSmallIcon(R.drawable.ic_input_add)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_input_add
                )
            )
            .setContentTitle("contentTitle")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.sym_call_missed,
                    "Cancel",
                    PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                )
            )
            .addAction(
                NotificationCompat.Action(
                    R.drawable.sym_call_outgoing,
                    "OK",
                    PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                )
            )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}