package com.example.baseproject.data

import android.net.Uri
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.ProfileRepository
import com.example.baseproject.domain.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl : ProfileRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    override suspend fun updateProfile(user: UserModel, profilePictureUri: Uri?): Response<Boolean> {
        return try {
            var url: String? = null
            if(profilePictureUri != null) {
                val storageRef = storage.reference
                val fileRef = storageRef.child("profile_pictures/${auth.uid}")
                fileRef.putFile(profilePictureUri).await()
                url = fileRef.downloadUrl.await().toString()
            }
            database.reference.child("users").child(auth.uid!!).child("profile").apply {
                child("display_name").setValue(user.displayName)
                child("phone_number").setValue(user.phoneNumber)
                child("birthday").setValue(user.birthday)
                if(url != null) child("profile_picture").setValue(url)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getProfile(): Response<UserModel> {
        return try {
            val snapshot = database.reference.child("users").child(auth.uid!!).child("profile").get().await()
            val user = UserModel(
                auth.uid!!,
                snapshot.child("display_name").value.toString(),
                snapshot.child("phone_number").value.toString(),
                snapshot.child("birthday").value.toString(),
                snapshot.child("email").value.toString(),
                snapshot.child("profile_picture").value.toString()
            )
            Response.Success(user)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}