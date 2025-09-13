package com.example.aroundegyptapplication.domain.usecase

import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.repository.ExperienceRepository
import javax.inject.Inject

class LikeExperienceUseCase @Inject constructor(
    private val repository: ExperienceRepository
) {
    suspend operator fun invoke(id: String): Experience {
        return repository.likeExperience(id)
    }
}