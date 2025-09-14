package com.example.aroundegyptapplication.domain.repository

import com.example.aroundegyptapplication.domain.model.Experience
import kotlinx.coroutines.flow.Flow

interface ExperienceRepository {
    suspend fun getRecommendedExperiences(): List<Experience>
    suspend fun getRecentExperiences(): List<Experience>
    suspend fun searchExperiences(query: String): List<Experience>
    suspend fun getExperienceById(id: String): Experience
    suspend fun likeExperience(id: String): Experience
}