package com.example.aroundegyptapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExperienceResponse(
    @SerializedName("data") val data: List<ExperienceDto>
)

data class SingleExperienceResponse(
    @SerializedName("data") val data: ExperienceDto
)

data class ExperienceDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("cover_photo") val coverPhoto: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("views_no") val viewsNo: Int = 0,
    @SerializedName("likes_no") val likesNo: Int = 0,
    @SerializedName("recommended") val recommended: Int = 0,
    @SerializedName("is_liked") val isLiked: Boolean? = null,
    @SerializedName("city") val city: CityDto? = null
)

data class CityDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
