package com.example.baseproject.data

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint


class AuthRepositoryImpl : AuthRepository {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    override suspend fun firebaseSignUp(email: String, password: String): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun firebaseLogin(email: String, password: String): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordResetEmail(email: String): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun logOut(): Response<Boolean> {
        TODO("Not yet implemented")
    }
}