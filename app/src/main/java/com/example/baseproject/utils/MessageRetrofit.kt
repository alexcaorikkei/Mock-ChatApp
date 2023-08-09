package com.example.baseproject.utils

import com.example.baseproject.domain.model.NotiModel
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

object MessageRetrofitHelper {
    private const val baseUrl = "https://fcm.googleapis.com/v1/projects/mock-chat-7a457/"
    const val token = "Bearer ya29.c.b0Aaekm1IdRIM6vd0AmJcJvj1csn7gNZ2J__npb578Z55G1ZPNKeHhbzSwE6rFxFEZfS6bYm40307mEK2R9B5HmpFqlF_Qh9uzpQTWDJrdTwo4_KNa-Yo0eAGKYJCzwDoe5mfrx_66FEjwcPsjw-913fDTeAzROhqTsrjMYcIcVCE9nO9tA_wa1yhvnMDH20b-oJxp1bCuWbO2HXPfGTkjPQ2aw0w8M9p-hMVnIY1h3_ZUMfPfdoURtmABHtgL87Ul9_m3fsqhPAHE0qulrzITLC2LSkBWECrOl0hgMzDJA-U5-nnX8Yj7Xje2QljnJferdrYSy6r55EI7lxPClSEbJ9oN352Af0whQVlBameFFvxW1yrX0Rra0s25Xx-ziZtw9k9bkUhbtpnu5qumuqh6pvtVQO3l3VqOrRs5j_b3hcvytoesYlI2SaOYo_lu8hkI2oogX6lqU-31dszI7lWtkfMh3R1W1FySIWU5RssBd7ftXcj4BnMJXnl-4yJJFqXIeR4_fhnZooFuddwsU6XO3od72pB_XXRI-giqa7gbOw29s8J1YUZ1JazaupWFzY21pI1byyjdeRpyVM76zngJjISye1evqZ2uz84M2110Xi3-b9cyvgYcf3ujBBubwWnbSRdIh9hXunfJ7zZbhbzk-5sXwURou39J5eXMdvqrysYn7yuY59if3kckW2xpdiY-pJwvqyOJ7et4j1owMM1eBjcigtbOJe42chWeRlqBIluYywX5ynei1gg4m1vo7Y8lU4Oy9O4h-a9vsU4zI4Iou8pZaIliF14lejU-gSFUqX4tQvXIZu1SV_-qrgYwbY39SrpMXzpZg3VQneFVzbsOhfsBspw9rBtQ3R65-nXOBX1gfBea-VkwIMFZ06kpXawnefFxV6_6ii5luzM8f6cIuXkf1q50mnW4336V1_VIbqlez4XIlt4fI7S8odO8M59f0QuYxR19jdwee8aIQcwi8YXUpW9J14Joq5Qzw1kr2FxRdbIxlc2I_gu"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

data class MessageResponseModel(
    @SerializedName("name")
    val name: String? = null
)

interface MessageRetrofitInterface {
    @POST("/messages:send")
    suspend fun postNotification(
        @retrofit2.http.Body notiModel: NotiModel,
        @retrofit2.http.Header("Authorization") authorization: String = MessageRetrofitHelper.token,
        @retrofit2.http.Header("Content-Type") contentType: String = "application/json"
    )
//    : Response<MessageResponseModel>
}

class MessageRetrofitUtil {
    companion object {
        val instance = MessageRetrofitHelper.getInstance().create(MessageRetrofitInterface::class.java)
    }
}