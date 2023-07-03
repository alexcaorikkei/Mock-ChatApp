package com.example.baseproject.di

import com.example.baseproject.data.AuthRepositoryImpl
import com.example.baseproject.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class AuthModule {
    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()
}