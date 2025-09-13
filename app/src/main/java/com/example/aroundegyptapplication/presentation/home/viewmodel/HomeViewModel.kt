package com.example.aroundegyptapplication.presentation.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aroundegyptapplication.di.IODispatcher
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.usecase.GetRecentExperiencesUseCase
import com.example.aroundegyptapplication.domain.usecase.GetRecommendedExperiencesUseCase
import com.example.aroundegyptapplication.domain.usecase.LikeExperienceUseCase
import com.example.aroundegyptapplication.domain.usecase.SearchExperiencesUseCase
import com.example.aroundegyptapplication.presentation.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecommendedExperiencesUseCase: GetRecommendedExperiencesUseCase,
    private val getRecentExperiencesUseCase: GetRecentExperiencesUseCase,
    private val searchExperiencesUseCase: SearchExperiencesUseCase,
    private val likeExperienceUseCase: LikeExperienceUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 500L
    }

    private var _state by mutableStateOf(HomeUiState())

    val state: State<HomeUiState>
        get() = derivedStateOf { _state }

    private var searchJob: Job? = null

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state = _state.copy(
            error = throwable.message ?: "An unexpected error occurred",
            isLoading = false,
            isRefreshing = false,
            isSearching = false
        )
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch(errorHandler + dispatcher) {
            _state = _state.copy(isLoading = true, error = null)

            try {
                val recommendedDeferred = async { getRecommendedExperiencesUseCase() }
                val recentDeferred = async { getRecentExperiencesUseCase() }

                val recommendedExperiences = recommendedDeferred.await()
                val recentExperiences = recentDeferred.await()

                _state = _state.copy(
                    recommendedExperiences = recommendedExperiences,
                    recentExperiences = recentExperiences,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state = _state.copy(
                    error = e.message ?: "Failed to load data",
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state = _state.copy(searchQuery = query)

        searchJob?.cancel()

        if (query.isBlank()) {
            clearSearch()
            return
        }

        // Debounced search
        searchJob = viewModelScope.launch(errorHandler + dispatcher) {
            _state = _state.copy(isSearching = true)
            delay(SEARCH_DEBOUNCE_MILLIS)

            try {
                val searchResults = searchExperiencesUseCase(query)
                _state = _state.copy(
                    searchResults = searchResults,
                    isSearching = false,
                    error = null
                )
            } catch (e: Exception) {
                _state = _state.copy(
                    error = e.message ?: "Search failed",
                    isSearching = false
                )
            }
        }
    }

    fun onSearchImeAction() {
        val query = _state.searchQuery
        if (query.isNotBlank()) {
            performImmediateSearch(query)
        }
    }

    private fun performImmediateSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(errorHandler + dispatcher) {
            _state = _state.copy(isSearching = true)

            try {
                val searchResults = searchExperiencesUseCase(query)
                _state = _state.copy(
                    searchResults = searchResults,
                    isSearching = false,
                    error = null
                )
            } catch (e: Exception) {
                _state = _state.copy(
                    error = e.message ?: "Search failed",
                    isSearching = false
                )
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _state = _state.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
    }

    fun onLikeExperience(experienceId: String) {
        viewModelScope.launch(errorHandler + dispatcher) {
            try {
                val updatedExperience = likeExperienceUseCase(experienceId)
                updateExperienceInAllLists(experienceId, updatedExperience)
            } catch (e: Exception) {
                _state = _state.copy(error = e.message ?: "Failed to like experience")
            }
        }
    }

    private fun updateExperienceInAllLists(experienceId: String, updatedExperience: Experience) {
        _state = _state.copy(
            recommendedExperiences = _state.recommendedExperiences.map { exp ->
                if (exp.id == experienceId) {
                    exp.copy(
                        isLiked = updatedExperience.isLiked,
                        likesNo = updatedExperience.likesNo
                    )
                } else exp
            },
            recentExperiences = _state.recentExperiences.map { exp ->
                if (exp.id == experienceId) {
                    exp.copy(
                        isLiked = updatedExperience.isLiked,
                        likesNo = updatedExperience.likesNo
                    )
                } else exp
            },
            searchResults = _state.searchResults.map { exp ->
                if (exp.id == experienceId) {
                    exp.copy(
                        isLiked = updatedExperience.isLiked,
                        likesNo = updatedExperience.likesNo
                    )
                } else exp
            }
        )
    }

    fun onRefresh() {
        if (_state.isRefreshing) return

        viewModelScope.launch(errorHandler + dispatcher) {
            _state = _state.copy(isRefreshing = true, error = null)

            try {
                val recommendedDeferred = async { getRecommendedExperiencesUseCase() }
                val recentDeferred = async { getRecentExperiencesUseCase() }

                val recommendedExperiences = recommendedDeferred.await()
                val recentExperiences = recentDeferred.await()

                _state = _state.copy(
                    recommendedExperiences = recommendedExperiences,
                    recentExperiences = recentExperiences,
                    isRefreshing = false
                )
            } catch (e: Exception) {
                _state = _state.copy(
                    error = e.message ?: "Failed to refresh data",
                    isRefreshing = false
                )
            }
        }
    }

    fun clearError() {
        _state = _state.copy(error = null)
    }

    fun retry() {
        clearError()
        loadInitialData()
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}