package com.example.baseproject.di

import com.example.baseproject.data.NotificationRepositoryImpl
import com.example.baseproject.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
class AppNotificationModule {
    @Provides
    fun provideNotificationRepository(): NotificationRepository = NotificationRepositoryImpl()
}