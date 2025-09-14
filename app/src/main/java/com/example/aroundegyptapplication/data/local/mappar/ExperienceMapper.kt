package com.example.aroundegyptapplication.data.local.mappar

import com.example.aroundegyptapplication.data.local.entity.ExperienceEntity
import com.example.aroundegyptapplication.data.remote.dto.ExperienceDto
import com.example.aroundegyptapplication.domain.model.Experience

object ExperienceMapper {

    fun ExperienceDto.toDomainModel(): Experience {
        return Experience(
            id = id,
            title = title,
            coverPhoto = coverPhoto,
            description = description ?: "",
            viewsNo = viewsNo,
            likesNo = likesNo,
            recommended = recommended == 1,
            isLiked = false,
            city = city?.name,
        )
    }
    
    fun ExperienceDto.toEntity(): ExperienceEntity {
        return ExperienceEntity(
            id = id,
            title = title,
            coverPhoto = coverPhoto,
            description = description,
            viewsNo = viewsNo,
            likesNo = likesNo,
            recommended = recommended == 1,
            isLiked = false,
            city = city?.name
        )
    }
    
    fun ExperienceEntity.toDomainModel(): Experience {
        return Experience(
            id = id,
            title = title,
            coverPhoto = coverPhoto,
            description = description ?: "",
            viewsNo = viewsNo,
            likesNo = likesNo,
            recommended = recommended,
            isLiked = isLiked,
            city = city,
        )
    }
    
    fun Experience.toEntity(): ExperienceEntity {
        return ExperienceEntity(
            id = id,
            title = title,
            coverPhoto = coverPhoto,
            description = description,
            viewsNo = viewsNo,
            likesNo = likesNo,
            recommended = recommended,
            isLiked = isLiked,
            city = city
        )
    }
}