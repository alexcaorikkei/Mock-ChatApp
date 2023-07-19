package com.example.baseproject.ui.home.detailchat.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.baseproject.R
import com.example.baseproject.container.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

const val id = 1
const val channelID = "channel1"

class Notification {
    companion object {
        @JvmStatic //nếu fun gọi từ class Java thì cần cho thêm Notification.companion.createNotification
        fun createNotification(id: Int, context: Context, title: String, message: String) {
            //ưu tiên hiển thị setStyle cuối cùng

            val isOpenApp = Intent(context, MainActivity::class.java)

            isOpenApp.putExtra("title1", title)
            isOpenApp.putExtra("message1", message)

            val pendingIntent = PendingIntent.getActivity(
                context, 1, isOpenApp, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                else
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            val imageUrl = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg"
            CoroutineScope(Dispatchers.IO).launch {
                try {//hien thi pic tu url
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input: InputStream = connection.inputStream
                    val imageBitmap = BitmapFactory.decodeStream(input)

                    withContext(Dispatchers.Main) {//hien thong bao tren thanh
                        val notification = NotificationCompat.Builder(context, channelID)
                            .setSmallIcon(R.drawable.ic_notification).setLargeIcon(imageBitmap)
                            .setContentTitle(title).setContentText(message)
                            .setContentIntent(pendingIntent)//ấn vào sẽ mở ứng dụng
                            .setOngoing(true)//gắn cứng thbao : kéo sang ngang k xóa đc
                            .setAutoCancel(true)//khi ấn thbao -> vào pj -> th.báo biến mất
                            .setStyle(//expand description
                                NotificationCompat.BigTextStyle().bigText(message)
                            )
                            .setChannelId(channelID).build()
                        val manager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.notify(id, notification)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}