package com.example.aroundegyptapplication.data.remote.api

import com.example.aroundegyptapplication.data.remote.dto.ExperienceResponse
import com.example.aroundegyptapplication.data.remote.dto.SingleExperienceResponse
import retrofit2.http.*

interface ExperienceApi {
    @GET("api/v2/experiences")
    suspend fun getRecentExperiences(): ExperienceResponse
    
    @GET("api/v2/experiences")
    suspend fun getRecommendedExperiences(
        @Query("filter[recommended]") recommended: Boolean = true
    ): ExperienceResponse
    
    @GET("api/v2/experiences")
    suspend fun searchExperiences(
        @Query("filter[title]") title: String
    ): ExperienceResponse
    
    @GET("api/v2/experiences/{id}")
    suspend fun getExperienceById(@Path("id") id: String): SingleExperienceResponse
    
    @POST("api/v2/experiences/{id}/like")
    suspend fun likeExperience(@Path("id") id: String): SingleExperienceResponse
}