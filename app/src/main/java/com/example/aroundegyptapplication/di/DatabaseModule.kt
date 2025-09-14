package com.example.aroundegyptapplication.di

import android.content.Context
import androidx.room.Room
import com.example.aroundegyptapplication.data.local.dao.ExperienceDao
import com.example.aroundegyptapplication.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "around_egypt_database"
        )
        .build()
    }
    
    @Provides
    @Singleton
    fun provideExperienceDao(database: AppDatabase): ExperienceDao {
        return database.experienceDao()
    }
}
