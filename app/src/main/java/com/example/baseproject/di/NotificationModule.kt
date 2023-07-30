package com.example.baseproject.di

import com.example.baseproject.data.NotificationRepositoryImpl
import com.example.baseproject.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class NotificationModule {
    @Provides
    fun provideNotificationRepository(): NotificationRepository = NotificationRepositoryImpl()
}