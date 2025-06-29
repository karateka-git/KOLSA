package com.example.kolsa.domain.repositories

import com.example.kolsa.domain.models.WorkoutList
import com.example.kolsa.domain.models.WorkoutId
import com.example.kolsa.domain.models.WorkoutItem
import com.example.kolsa.domain.models.WorkoutType
import com.example.kolsa.domain.models.WorkoutVideo

interface WorkoutRepository {
    suspend fun getWorkoutList(
        searchQuery: String = "",
        selectedFilterType: WorkoutType = WorkoutType.Unknown,
    ): Result<WorkoutList>
    suspend fun getWorkout(id: WorkoutId): Result<WorkoutItem>
    suspend fun getWorkoutVideo(id: WorkoutId): Result<WorkoutVideo>
}
