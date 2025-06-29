package com.example.kolsa.domain.interactors

import com.example.kolsa.domain.models.WorkoutDetailInfo
import com.example.kolsa.domain.models.WorkoutId
import com.example.kolsa.domain.models.WorkoutItem
import com.example.kolsa.domain.models.WorkoutVideo
import com.example.kolsa.domain.repositories.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class LoadDetailWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(id: WorkoutId): Result<WorkoutDetailInfo> = withContext(Dispatchers.IO) {
        val workoutInfoDeferred = async {
            repository.getWorkout(id)
        }
        val workoutVideDeferred = async {
            repository.getWorkoutVideo(id)
        }

        val workoutInfo = workoutInfoDeferred.await()
        val workoutVideo = workoutVideDeferred.await()

        when {
            workoutInfo.isSuccess && workoutVideo.isSuccess -> {
                Result.success(
                    WorkoutDetailInfo(
                        id = id,
                        info = workoutInfo.getOrDefault(WorkoutItem()),
                        video = workoutVideo.getOrDefault(WorkoutVideo()),
                    )
                )
            }
            else -> Result.failure(
                workoutInfo.exceptionOrNull() ?:
                workoutVideo.exceptionOrNull() ?:
                Exception("Something went wrong..")
            )
        }
    }
}
