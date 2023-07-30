package com.example.baseproject.domain.repository

interface NotificationRepository {
    fun registerNotificationToken(token: String)
    fun unregisterNotificationToken()
}