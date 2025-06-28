package com.example.kolsa.domain.repositories

import com.example.kolsa.domain.models.WorkoutList
import com.example.kolsa.domain.models.WorkoutId
import com.example.kolsa.domain.models.WorkoutItem
import com.example.kolsa.domain.models.WorkoutType

interface WorkoutRepository {
    suspend fun getWorkoutList(): Result<WorkoutList>
    suspend fun changeSelectedFilter(selectedFilterType: WorkoutType): Result<WorkoutList>
    suspend fun getWorkout(id: WorkoutId): Result<WorkoutItem>
}
