package com.example.aroundegyptapplication.presentation.experience.model

import com.example.aroundegyptapplication.domain.model.Experience

data class ExperienceUiState(
    val experience: Experience? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)