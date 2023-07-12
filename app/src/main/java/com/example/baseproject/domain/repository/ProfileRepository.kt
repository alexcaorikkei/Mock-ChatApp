package com.example.baseproject.domain.repository
import android.net.Uri
import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.profile.model.UserModel

interface ProfileRepository {
    suspend fun updateProfile(user : UserModel, profilePictureUri: Uri?): Response<Boolean>
    suspend fun getProfile(): Response<UserModel>
}