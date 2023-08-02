package com.example.baseproject.domain.repository

import androidx.lifecycle.LiveData
import com.example.baseproject.domain.model.NotificationModel

interface NotificationRepository {
    fun getNotification(): LiveData<NotificationModel>
    fun canOpenNotification(): Boolean
}
