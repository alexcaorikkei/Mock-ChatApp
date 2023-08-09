package com.example.baseproject.domain.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("title") val title: String? = null,
    @SerializedName("body") val body: String? = null,
)
data class MessageModel (
    @SerializedName("token") val token: String? = null,
    @SerializedName("notification") val notification: Notification? = null
)

data class NotiModel (
    @SerializedName("message") val message: MessageModel? = null
)