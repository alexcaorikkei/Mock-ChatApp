package com.example.baseproject.di

import com.example.baseproject.data.RoomRepositoryImpl
import com.example.baseproject.domain.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class MessageModule {
    @Provides
    fun provideMessageRepository(): RoomRepository = RoomRepositoryImpl()
}