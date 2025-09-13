package com.example.aroundegyptapplication.domain.usecase

import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.repository.ExperienceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchExperiencesUseCase @Inject constructor(
    private val repository: ExperienceRepository
) {
    suspend operator fun invoke(query: String): List<Experience> {
        return if (query.isBlank()) {
            repository.getRecentExperiences()
        } else {
            repository.searchExperiences(query)
        }
    }
}