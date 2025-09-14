package com.example.aroundegyptapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experiences")
data class ExperienceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val coverPhoto: String,
    val description: String?,
    val viewsNo: Int,
    val likesNo: Int,
    val recommended: Boolean,
    val isLiked: Boolean = false,
    val city: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)