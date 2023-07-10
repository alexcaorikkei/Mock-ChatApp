package com.example.baseproject.di

import com.example.baseproject.data.DetailMessageRepositoryImpl
import com.example.baseproject.domain.repository.DetailMessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DetailMessageModule {
    @Provides
    fun provideMessageRepository(): DetailMessageRepository = DetailMessageRepositoryImpl()
}