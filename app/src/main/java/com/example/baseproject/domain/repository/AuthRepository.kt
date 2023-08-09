package com.example.baseproject.domain.repository

import com.example.baseproject.domain.model.Response

interface AuthRepository {
    suspend fun firebaseSignUp(email: String, password: String, displayName: String): Response<Boolean>
    suspend fun firebaseLogin(email: String, password: String, notificationToken: String): Response<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Response<Boolean>
    suspend fun logOut(): Response<Boolean>
    fun isLogin(): Boolean
}