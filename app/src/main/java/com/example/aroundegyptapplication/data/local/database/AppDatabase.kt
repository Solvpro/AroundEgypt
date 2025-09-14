package com.example.aroundegyptapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aroundegyptapplication.data.local.dao.ExperienceDao
import com.example.aroundegyptapplication.data.local.entity.ExperienceEntity

@Database(
    entities = [ExperienceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}