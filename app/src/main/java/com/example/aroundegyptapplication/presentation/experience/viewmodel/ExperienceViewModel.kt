package com.example.aroundegyptapplication.presentation.experience.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aroundegyptapplication.di.IODispatcher
import com.example.aroundegyptapplication.di.MainDispatcher
import com.example.aroundegyptapplication.domain.usecase.GetExperienceDetailUseCase
import com.example.aroundegyptapplication.domain.usecase.LikeExperienceUseCase
import com.example.aroundegyptapplication.presentation.experience.model.ExperienceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExperienceDetailUseCase: GetExperienceDetailUseCase,
    private val likeExperienceUseCase: LikeExperienceUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val experienceId: String = checkNotNull(savedStateHandle["experienceId"])

    private var _state by mutableStateOf(ExperienceUiState())

    val state: State<ExperienceUiState>
        get() = derivedStateOf { _state }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state = _state.copy(
            error = throwable.message ?: "An unexpected error occurred",
            isLoading = false
        )
    }

    init {
        loadExperience()
    }

    private fun loadExperience() {
        viewModelScope.launch(errorHandler + dispatcher) {
            _state = _state.copy(isLoading = true, error = null)

            try {
                val experience = getExperienceDetailUseCase(experienceId)
                _state = _state.copy(
                    experience = experience,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state = _state.copy(
                    error = e.message ?: "Failed to load experience",
                    isLoading = false
                )
            }
        }
    }

    fun onLikeExperience() {
        val currentExperience = _state.experience ?: return

        if (!currentExperience.isLiked) {
            viewModelScope.launch(errorHandler + dispatcher) {
                try {
                    val updatedExperience = likeExperienceUseCase(experienceId)
                    _state = _state.copy(
                        experience = currentExperience.copy(
                            isLiked = true,
                            likesNo = updatedExperience.likesNo
                        )
                    )
                } catch (e: Exception) {
                    _state = _state.copy(
                        error = e.message ?: "Failed to like experience"
                    )
                }
            }
        }
    }

    fun hideDetails() {
        _state = _state.copy(showDetails = false)
    }

    fun clearError() {
        _state = _state.copy(error = null)
    }

    fun retry() {
        clearError()
        loadExperience()
    }
}