package com.example.kolsa.data.services

import com.example.kolsa.data.models.response.WorkoutItemResponse
import retrofit2.http.GET

interface WorkoutApi {
    @GET("get_workouts")
    suspend fun getWorkoutList(): List<WorkoutItemResponse>
}
