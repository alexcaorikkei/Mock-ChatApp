package com.example.baseproject.di

import com.example.baseproject.data.MessageRepositoryImpl
import com.example.baseproject.domain.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ViewModelComponent::class)
class MessageModule {
    @Provides
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()
}