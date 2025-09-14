package com.example.aroundegyptapplication.domain.usecase

import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.repository.ExperienceRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class GetRecommendedExperiencesUseCaseTest {

    @Mock
    private lateinit var repository: ExperienceRepository
    
    private lateinit var useCase: GetRecommendedExperiencesUseCase

    @Before
    fun setup() {
        useCase = GetRecommendedExperiencesUseCase(repository)
    }

    @Test
    fun `invoke - calls repository with correct parameters`() = runTest {
        // Given
        val forceRefresh = true
        val expectedExperiences = listOf(
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
        
        whenever(repository.getRecommendedExperiences()).thenReturn(expectedExperiences)

        // When
        val result = useCase()

        // Then
        assertEquals(expectedExperiences, result)
    }
}