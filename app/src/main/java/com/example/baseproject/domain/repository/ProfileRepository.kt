package com.example.baseproject.domain.repository
import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.profile.model.UserModel

interface ProfileRepository {
    suspend fun updateProfile(user : UserModel): Response<Boolean>
    suspend fun getProfile(): Response<UserModel>
}