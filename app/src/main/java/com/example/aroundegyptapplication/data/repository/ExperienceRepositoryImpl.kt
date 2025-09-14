package com.example.aroundegyptapplication.data.repository

import com.example.aroundegyptapplication.data.local.dao.ExperienceDao
import com.example.aroundegyptapplication.data.local.mappar.ExperienceMapper.toDomainModel
import com.example.aroundegyptapplication.data.local.mappar.ExperienceMapper.toEntity
import com.example.aroundegyptapplication.data.remote.api.ExperienceApi
import com.example.aroundegyptapplication.di.IODispatcher
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.repository.ExperienceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExperienceRepositoryImpl @Inject constructor(
    private val api: ExperienceApi,
    private val dao: ExperienceDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ExperienceRepository {

    override suspend fun getRecommendedExperiences(): List<Experience> = withContext(dispatcher) {
        try {
            val response = api.getRecommendedExperiences()
            val experiences = response.data.map { it.toDomainModel() }

            saveRecommendedExperiencesToCache(response.data.map { it.toEntity() })

            experiences
        } catch (e: Exception) {
            val cachedExperiences = getCachedRecommendedExperiences()
            cachedExperiences.ifEmpty {
                throw e
            }
        }
    }

    override suspend fun getRecentExperiences(): List<Experience> = withContext(dispatcher) {
        try {
            val response = api.getRecentExperiences()
            val experiences = response.data.map { it.toDomainModel() }

            saveRecentExperiencesToCache(response.data.map { it.toEntity().copy(recommended = false) })

            experiences
        } catch (e: Exception) {
            val cachedExperiences = getCachedRecentExperiences()
            cachedExperiences.ifEmpty {
                throw e
            }
        }
    }

    override suspend fun searchExperiences(query: String): List<Experience> = withContext(dispatcher) {
        try {
            val response = api.searchExperiences(query)
            val experiences = response.data.map { it.toDomainModel() }

            val entities = response.data.map { it.toEntity() }
            dao.insertExperiences(entities)

            experiences
        } catch (e: Exception) {
            val cachedResults = dao.searchExperiences(query)
                .map { entities -> entities.map { it.toDomainModel() } }
                .first()

            cachedResults.ifEmpty {
                throw e
            }
        }
    }

    override suspend fun getExperienceById(id: String): Experience = withContext(dispatcher) {
        try {
            val response = api.getExperienceById(id)
            val experience = response.data.toDomainModel()

            dao.insertExperience(response.data.toEntity())

            experience
        } catch (e: Exception) {
            val cachedExperience = dao.getExperienceById(id)
                .map { entity -> entity?.toDomainModel() }
                .first()

            cachedExperience ?: throw e
        }
    }

    override suspend fun likeExperience(id: String): Experience = withContext(dispatcher) {
        try {
            val response = api.likeExperience(id)
            val experience = response.data.toDomainModel().copy(isLiked = true)
            dao.insertExperience(experience.toEntity())
            experience
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getCachedRecommendedExperiences(): List<Experience> {
        return try {
            dao.getRecommendedExperiences()
                .map { entities -> entities.map { it.toDomainModel() } }
                .first()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getCachedRecentExperiences(): List<Experience> {
        return try {
            dao.getRecentExperiences()
                .map { entities -> entities.map { it.toDomainModel() } }
                .first()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun saveRecommendedExperiencesToCache(entities: List<com.example.aroundegyptapplication.data.local.entity.ExperienceEntity>) {
        try {
            dao.deleteExperiencesByType(recommended = true)
            dao.insertExperiences(entities)
        } catch (e: Exception) {
            println("Failed to save recommended experiences to cache: ${e.message}")
        }
    }

    private suspend fun saveRecentExperiencesToCache(entities: List<com.example.aroundegyptapplication.data.local.entity.ExperienceEntity>) {
        try {
            dao.deleteExperiencesByType(recommended = false)
            dao.insertExperiences(entities)
        } catch (e: Exception) {
            println("Failed to save recent experiences to cache: ${e.message}")
        }
    }

}