package com.example.baseproject.data

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await


class AuthRepositoryImpl : AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override suspend fun firebaseSignUp(email: String, password: String, displayName: String): Response<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            database.reference.child("users")
                .child(auth.currentUser!!.uid)
                .child("profile")
                .apply {
                    child("email").setValue(email)
                    child("display_name").setValue(displayName)
                    child("profile_picture").setValue("")
                    child("birthday").setValue("")
                    child("phone_number").setValue("")
                }
//                createDemoData()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun firebaseLogin(email: String, password: String, notificationToken: String): Response<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            database.reference
                .child("users")
                .child(auth.currentUser!!.uid)
                .child("notification")
                .setValue(null).await()
            database.reference.child("users")
                .child(auth.currentUser!!.uid)
                .child("device-token")
                .setValue(notificationToken).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Response<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun logOut(): Response<Boolean> {
        return try {
            database.reference.child("users")
                .child(auth.currentUser!!.uid)
                .child("notification")
                .setValue(null).await()
            database.reference.child("users")
                .child(auth.currentUser!!.uid)
                .child("device-token")
                .setValue(null).await()
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun isLogin(): Boolean {
        return auth.currentUser != null
    }
}