package com.example.baseproject.domain.repository
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.model.UserModel

interface ProfileRepository {
    suspend fun updateProfile(user : UserModel, profilePictureUri: Uri?): Response<Boolean>
    fun getProfile(): MutableLiveData<Response<UserModel>>
}