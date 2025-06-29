package com.example.kolsa.data.services

import com.example.kolsa.data.models.response.WorkoutItemResponse
import com.example.kolsa.data.models.response.WorkoutVideoResponse
import com.example.kolsa.domain.models.WorkoutId
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkoutApi {
    @GET("get_workouts")
    suspend fun getWorkoutList(): List<WorkoutItemResponse>

    @GET("get_video")
    suspend fun getWorkoutVideo(@Query("id") id: WorkoutId): WorkoutVideoResponse
}
