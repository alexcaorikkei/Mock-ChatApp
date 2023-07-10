package com.example.baseproject.data

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.ProfileRepository
import com.example.baseproject.ui.home.profile.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl : ProfileRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override suspend fun updateProfile(user: UserModel): Response<Boolean> {
        TODO("Not yet implemented")
        return try {
            database.reference.child("users").child(auth.uid!!).child("profile").apply {
                child("display_name").setValue(user.displayName)
                child("phone_number").setValue(user.phoneNumber)
                child("birthday").setValue(user.birthday)
                child("profile_picture").setValue(user.profilePicture)
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