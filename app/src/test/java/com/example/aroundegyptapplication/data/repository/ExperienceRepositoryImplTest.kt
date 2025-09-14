package com.example.aroundegyptapplication.data.repository

import com.example.aroundegyptapplication.data.local.dao.ExperienceDao
import com.example.aroundegyptapplication.data.local.entity.ExperienceEntity
import com.example.aroundegyptapplication.data.remote.api.ExperienceApi
import com.example.aroundegyptapplication.data.remote.dto.CityDto
import com.example.aroundegyptapplication.data.remote.dto.ExperienceDto
import com.example.aroundegyptapplication.data.remote.dto.ExperienceResponse
import com.example.aroundegyptapplication.domain.model.Experience
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ExperienceRepositoryImplTest {

    @Mock
    private lateinit var api: ExperienceApi

    @Mock
    private lateinit var dao: ExperienceDao

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: ExperienceRepositoryImpl

    @Before
    fun setup() {
        repository = ExperienceRepositoryImpl(api, dao, testDispatcher)
    }

    @Test
    fun `getRecommendedExperiences - success from API - saves to cache and returns data`() =
        runTest {
            // Given
            val mockApiResponse = createMockApiResponse()
            val expectedExperiences = createMockExperiences()

            whenever(api.getRecommendedExperiences()).thenReturn(mockApiResponse)

            // When
            val result = repository.getRecommendedExperiences()

            // Then
            assertEquals(expectedExperiences, result)
            verify(dao).deleteExperiencesByType(recommended = true)
            verify(dao).insertExperiences(any())
        }

    @Test
    fun `getRecommendedExperiences - API fails - returns cached data`() = runTest {
        // Given
        val cachedExperiences = createMockExperiences()
        val cachedEntities = createMockExperienceEntities()

        whenever(api.getRecommendedExperiences()).thenThrow(RuntimeException("Network error"))
        whenever(dao.getRecommendedExperiences()).thenReturn(flowOf(cachedEntities))

        // When
        val result = repository.getRecommendedExperiences()

        // Then
        assertEquals(cachedExperiences, result)
        verify(dao, never()).insertExperiences(any())
    }

    @Test
    fun `searchExperiences - API success - caches and returns results`() = runTest {
        // Given
        val query = "temple"
        val mockApiResponse = createMockApiResponse()
        val expectedExperiences = createMockExperiences()

        whenever(api.searchExperiences(query)).thenReturn(mockApiResponse)

        // When
        val result = repository.searchExperiences(query)

        // Then
        assertEquals(expectedExperiences, result)
        verify(dao).insertExperiences(any())
    }

    @Test
    fun `searchExperiences - API fails - returns cached search results`() = runTest {
        // Given
        val query = "temple"
        val cachedExperiences = createMockExperiences()
        val cachedEntities = createMockExperienceEntities()

        whenever(api.searchExperiences(query)).thenThrow(RuntimeException("Network error"))
        whenever(dao.searchExperiences(query)).thenReturn(flowOf(cachedEntities))

        // When
        val result = repository.searchExperiences(query)

        // Then
        assertEquals(cachedExperiences, result)
    }

    // Helper methods
    private fun createMockExperiences(): List<Experience> {
        return listOf(
            Experience(
                id = "1",
                title = "Test Experience",
                description = "Test Description",
                coverPhoto = "https://test.com/image.jpg",
                city = "Cairo",
                viewsNo = 100,
                likesNo = 50,
                isLiked = false,
                recommended = true
            )
        )
    }

    private fun createMockExperienceEntities(): List<ExperienceEntity> {
        // Create mock entities based on your entity structure
        return listOf(
            ExperienceEntity(
                id = "1",
                title = "Test Experience",
                description = "Test Description",
                coverPhoto = "https://test.com/image.jpg",
                city = "Cairo",
                viewsNo = 100,
                likesNo = 50,
                isLiked = false,
                recommended = true
            )
        )
    }

    private fun createMockApiResponse(): ExperienceResponse {
        return ExperienceResponse(data = createMockApiData())
    }

    private fun createMockApiData(): List<ExperienceDto> {
        return listOf(
            ExperienceDto(
                id = "1",
                title = "Test Experience",
                description = "Test Description",
                coverPhoto = "https://test.com/image.jpg",
                city = CityDto(
                    id = 1,
                    "Cairo"
                ),
                viewsNo = 100,
                likesNo = 50,
                isLiked = false,
                recommended = 1
            )
        )
    }
}