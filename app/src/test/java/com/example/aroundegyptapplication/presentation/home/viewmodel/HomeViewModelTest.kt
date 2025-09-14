package com.example.aroundegyptapplication.presentation.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.usecase.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getRecommendedExperiencesUseCase: GetRecommendedExperiencesUseCase

    @Mock
    private lateinit var getRecentExperiencesUseCase: GetRecentExperiencesUseCase

    @Mock
    private lateinit var searchExperiencesUseCase: SearchExperiencesUseCase

    @Mock
    private lateinit var likeExperienceUseCase: LikeExperienceUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        // Don't create ViewModel here - each test will create its own with specific mocking
    }

    @Test
    fun `loadInitialData - success - updates state with data`() = runTest {
        // Given
        val recommendedExperiences = createMockExperiences("recommended")
        val recentExperiences = createMockExperiences("recent")

        // Mock both use cases with the parameter they expect
        whenever(getRecommendedExperiencesUseCase()).thenReturn(recommendedExperiences)
        whenever(getRecentExperiencesUseCase()).thenReturn(recentExperiences)

        // When
        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(recommendedExperiences, state.recommendedExperiences)
        assertEquals(recentExperiences, state.recentExperiences)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadInitialData - error - updates state with error`() = runTest {
        // Given
        val errorMessage = "Network error"

        // Only stub what will actually be called - when first call fails, the second may not be called
        whenever(getRecommendedExperiencesUseCase())
            .thenThrow(RuntimeException(errorMessage))

        // When
        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `onSearchQueryChanged - updates search query and triggers search`() = runTest(testDispatcher) {
        // Given
        val query = "temple"
        val searchResults = createMockExperiences("search")

        // Setup initial data mocks
        whenever(getRecommendedExperiencesUseCase()).thenReturn(emptyList())
        whenever(getRecentExperiencesUseCase()).thenReturn(emptyList())
        whenever(searchExperiencesUseCase(query)).thenReturn(searchResults)

        // Create ViewModel first
        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )
        advanceUntilIdle() // Complete initial loading

        // When
        viewModel.onSearchQueryChanged(query)
        advanceTimeBy(500L) // Wait for debounce
        advanceUntilIdle()  // Complete search

        // Then
        val state = viewModel.state.value
        assertEquals(query, state.searchQuery)
        assertEquals(searchResults, state.searchResults)
        assertFalse(state.isSearching)

        // Verify the search use case was called
        verify(searchExperiencesUseCase).invoke(query)
    }

    @Test
    fun `clearSearch - resets search state`() = runTest {
        // Given
        whenever(getRecommendedExperiencesUseCase()).thenReturn(emptyList())
        whenever(getRecentExperiencesUseCase()).thenReturn(emptyList())

        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // Set up some search state first
        viewModel.onSearchQueryChanged("temple")

        // When
        viewModel.clearSearch()

        // Then
        val state = viewModel.state.value
        assertEquals("", state.searchQuery)
        assertTrue(state.searchResults.isEmpty())
        assertFalse(state.isSearching)
    }

    @Test
    fun `onLikeExperience - success - updates experience in all lists`() = runTest {
        // Given
        val experienceId = "1"
        val originalExperience = createMockExperience(experienceId, isLiked = false, likesNo = 50)
        val updatedExperience = createMockExperience(experienceId, isLiked = true, likesNo = 51)

        // Setup initial state
        whenever(getRecommendedExperiencesUseCase())
            .thenReturn(listOf(originalExperience))
        whenever(getRecentExperiencesUseCase()).thenReturn(emptyList())
        whenever(likeExperienceUseCase(experienceId)).thenReturn(updatedExperience)

        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // When
        viewModel.onLikeExperience(experienceId)

        // Then
        val state = viewModel.state.value
        val likedExperience = state.recommendedExperiences.find { it.id == experienceId }
        assertTrue(likedExperience?.isLiked == true)
        assertEquals(51, likedExperience?.likesNo)
    }

    @Test
    fun `onRefresh - success - updates data and resets refresh state`() = runTest {
        // Given
        val initialRecommended = createMockExperiences("initial_recommended")
        val initialRecent = createMockExperiences("initial_recent")
        val refreshRecommended = createMockExperiences("refresh_recommended")
        val refreshRecent = createMockExperiences("refresh_recent")

        // Setup initial load
        whenever(getRecommendedExperiencesUseCase())
            .thenReturn(initialRecommended)
            .thenReturn(refreshRecommended) // Second call for refresh
        whenever(getRecentExperiencesUseCase())
            .thenReturn(initialRecent)
            .thenReturn(refreshRecent) // Second call for refresh

        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // When
        viewModel.onRefresh()

        // Then
        val state = viewModel.state.value
        assertFalse(state.isRefreshing)
        assertEquals(refreshRecommended, state.recommendedExperiences)
        assertEquals(refreshRecent, state.recentExperiences)
    }

    @Test
    fun `onSearchQueryChanged - empty query - clears search immediately`() = runTest {
        // Given
        whenever(getRecommendedExperiencesUseCase()).thenReturn(emptyList())
        whenever(getRecentExperiencesUseCase()).thenReturn(emptyList())

        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // Set some search state first
        viewModel.onSearchQueryChanged("temple")

        // When - Clear with empty query
        viewModel.onSearchQueryChanged("")

        // Then
        val state = viewModel.state.value
        assertEquals("", state.searchQuery)
        assertTrue(state.searchResults.isEmpty())
        assertFalse(state.isSearching)
    }

    @Test
    fun `search error - updates error state`() = runTest(testDispatcher) {
        // Given
        val query = "temple"
        val errorMessage = "Search failed"

        whenever(getRecommendedExperiencesUseCase()).thenReturn(emptyList())
        whenever(getRecentExperiencesUseCase()).thenReturn(emptyList())
        whenever(searchExperiencesUseCase(query)).thenThrow(RuntimeException(errorMessage))

        viewModel = HomeViewModel(
            getRecommendedExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperienceUseCase,
            testDispatcher
        )
        advanceUntilIdle()

        // When
        viewModel.onSearchQueryChanged(query)
        advanceTimeBy(500L)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(errorMessage, state.error)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    // Helper methods
    private fun createMockExperiences(type: String): List<Experience> {
        return listOf(
            createMockExperience("${type}_1"),
            createMockExperience("${type}_2")
        )
    }

    private fun createMockExperience(
        id: String,
        isLiked: Boolean = false,
        likesNo: Int = 50
    ): Experience {
        return Experience(
            id = id,
            title = "Test Experience $id",
            description = "Test Description",
            coverPhoto = "https://test.com/image.jpg",
            city = "Cairo",
            viewsNo = 100,
            likesNo = likesNo,
            isLiked = isLiked,
            recommended = true
        )
    }
}