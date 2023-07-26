package com.example.baseproject.data

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.ProfileRepository
import com.example.baseproject.domain.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import timber.log.Timber

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

    override fun getProfile(): MutableLiveData<Response<UserModel>> {
        val profileResponse = MutableLiveData<Response<UserModel>>()
        database.reference.child("users").child(auth.uid!!).child("profile").addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val user = UserModel(
                    displayName = snapshot.child("display_name").value.toString(),
                    email = snapshot.child("email").value.toString(),
                    phoneNumber = snapshot.child("phone_number").value.toString(),
                    birthday = snapshot.child("birthday").value.toString(),
                    profilePicture = snapshot.child("profile_picture").value.toString()
                )
                profileResponse.postValue(Response.Success(user))
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                profileResponse.postValue(Response.Failure(error.toException()))
            }
        })
        return profileResponse
    }
}