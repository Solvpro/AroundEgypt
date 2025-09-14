package com.example.aroundegyptapplication.data.local.dao

import androidx.room.*
import com.example.aroundegyptapplication.data.local.entity.ExperienceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperienceDao {
    
    @Query("SELECT * FROM experiences WHERE recommended = 1 ORDER BY lastUpdated DESC")
    fun getRecommendedExperiences(): Flow<List<ExperienceEntity>>
    
    @Query("SELECT * FROM experiences WHERE recommended = 0 ORDER BY lastUpdated DESC")
    fun getRecentExperiences(): Flow<List<ExperienceEntity>>
    
    @Query("SELECT * FROM experiences WHERE title LIKE '%' || :query || '%'")
    fun searchExperiences(query: String): Flow<List<ExperienceEntity>>
    
    @Query("SELECT * FROM experiences WHERE id = :id")
    fun getExperienceById(id: String): Flow<ExperienceEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperiences(experiences: List<ExperienceEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience(experience: ExperienceEntity)
    
    @Update
    suspend fun updateExperience(experience: ExperienceEntity)
    
    @Query("DELETE FROM experiences WHERE recommended = :recommended")
    suspend fun deleteExperiencesByType(recommended: Boolean)
}