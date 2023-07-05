package com.example.baseproject.data

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.AuthRepository
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
                }
                createDemoData()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    }

    private fun createDemoData() {
        database.reference.child("users")
            .child(auth.currentUser!!.uid).apply {
                child("friends").apply {
                    child("001").apply {
                        child("display_name").setValue("John Doe")
                        child("profile_picture").setValue("")
                    }
                    child("002").apply {
                        child("display_name").setValue("Jane Doe")
                        child("profile_picture").setValue("")
                    }
                    child("003").apply {
                        child("display_name").setValue("John Smith")
                        child("profile_picture").setValue("")
                    }
                    child("004").apply {
                        child("display_name").setValue("Jane Smith")
                        child("profile_picture").setValue("")
                    }
                    child("005").apply {
                        child("display_name").setValue("John Doe Jr.")
                        child("profile_picture").setValue("")
                    }
                }
                child("added").apply {
                    child("006").apply {
                        child("display_name").setValue("John Doe")
                        child("profile_picture").setValue("")
                    }
                    child("007").apply {
                        child("display_name").setValue("Jane Doe")
                        child("profile_picture").setValue("")
                    }
                    child("008").apply {
                        child("display_name").setValue("John Smith")
                        child("profile_picture").setValue("")
                    }
                    child("009").apply {
                        child("display_name").setValue("Jane Smith")
                        child("profile_picture").setValue("")
                    }
                    child("010").apply {
                        child("display_name").setValue("John Doe Jr.")
                        child("profile_picture").setValue("")
                    }
                }
                child("request").apply {
                    child("011").apply {
                        child("display_name").setValue("John Doe")
                        child("profile_picture").setValue("")
                    }
                    child("012").apply {
                        child("display_name").setValue("Jane Doe")
                        child("profile_picture").setValue("")
                    }
                    child("013").apply {
                        child("display_name").setValue("John Smith")
                        child("profile_picture").setValue("")
                    }
                    child("014").apply {
                        child("display_name").setValue("Jane Smith")
                        child("profile_picture").setValue("")
                    }
                    child("015").apply {
                        child("display_name").setValue("John Doe Jr.")
                        child("profile_picture").setValue("")
                    }
                }
            }
    }

    override suspend fun firebaseLogin(email: String, password: String): Response<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
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
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}