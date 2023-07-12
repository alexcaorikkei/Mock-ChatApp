package com.example.baseproject.di

import com.example.baseproject.data.FriendRepositoryImpl
import com.example.baseproject.domain.repository.FriendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FriendModule {
    @Provides
    fun provideFriendRepository(): FriendRepository = FriendRepositoryImpl()
}