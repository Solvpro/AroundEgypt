package com.example.aroundegyptapplication.di

import com.example.aroundegyptapplication.data.repository.ExperienceRepositoryImpl
import com.example.aroundegyptapplication.domain.repository.ExperienceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindExperienceRepository(
        experienceRepositoryImpl: ExperienceRepositoryImpl
    ): ExperienceRepository

}