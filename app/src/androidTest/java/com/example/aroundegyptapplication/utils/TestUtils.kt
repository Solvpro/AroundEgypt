package com.example.aroundegyptapplication.utils

import com.example.aroundegyptapplication.domain.model.Experience

object TestUtils {
    
    fun createMockExperience(
        id: String = "test_id",
        title: String = "Test Experience",
        isLiked: Boolean = false,
        recommended: Boolean = true
    ): Experience {
        return Experience(
            id = id,
            title = title,
            description = "Test description for $title",
            coverPhoto = "https://example.com/image.jpg",
            city = "Test City",
            viewsNo = 1000,
            likesNo = 50,
            isLiked = isLiked,
            recommended = recommended
        )
    }

    fun createMockExperiences(count: Int): List<Experience> {
        return (1..count).map { index ->
            createMockExperience(
                id = "test_$index",
                title = "Test Experience $index"
            )
        }
    }
}