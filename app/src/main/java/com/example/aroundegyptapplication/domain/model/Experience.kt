package com.example.aroundegyptapplication.domain.model

data class Experience(
    val id: String,
    val title: String,
    val coverPhoto: String,
    val description: String = "",
    val viewsNo: Int = 0,
    val likesNo: Int = 0,
    val recommended: Boolean = false,
    val hasVideo: Boolean = false,
    val isLiked: Boolean = false,
    val city: String? = null
)