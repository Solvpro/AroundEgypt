package com.example.aroundegyptapplication.presentation.home.model

import com.example.aroundegyptapplication.domain.model.Experience

data class HomeUiState(
    val recommendedExperiences: List<Experience> = emptyList(),
    val recentExperiences: List<Experience> = emptyList(),
    val searchResults: List<Experience> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    val isRefreshing: Boolean = false
)