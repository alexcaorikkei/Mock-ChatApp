package com.example.baseproject.di

import com.example.baseproject.data.ProfileRepositoryImpl
import com.example.baseproject.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ProfileModule {
    @Provides
    fun provideProfileRepository(): ProfileRepository = ProfileRepositoryImpl()
}