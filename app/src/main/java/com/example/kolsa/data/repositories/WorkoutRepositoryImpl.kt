package com.example.kolsa.data.repositories

import com.example.kolsa.data.models.response.toWorkoutItem
import com.example.kolsa.data.models.response.toWorkoutVideo
import com.example.kolsa.data.services.WorkoutApi
import com.example.kolsa.domain.models.WorkoutId
import com.example.kolsa.domain.models.WorkoutItem
import com.example.kolsa.domain.models.WorkoutList
import com.example.kolsa.domain.models.WorkoutType
import com.example.kolsa.domain.models.WorkoutVideo
import com.example.kolsa.domain.repositories.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class WorkoutRepositoryImpl(
    private val service: WorkoutApi
) : WorkoutRepository, KoinComponent {
    private var cashedWorkoutList: WorkoutList = WorkoutList()

    override suspend fun getWorkoutList(): Result<WorkoutList> = withContext(Dispatchers.IO) {
        runCatching {
            val items = service.getWorkoutList().map { it.toWorkoutItem() }
            // TODO просто чтобы было побольше элементов
            WorkoutList(items = items + items + items).also {
                cashedWorkoutList = it
            }
        }
    }

    override suspend fun changeSelectedFilter(selectedFilterType: WorkoutType): Result<WorkoutList> = withContext(Dispatchers.Default) {
        runCatching {
            WorkoutList(
                items = cashedWorkoutList.items.filter {
                    when(selectedFilterType) {
                        WorkoutType.Unknown -> true
                        else -> it.type == selectedFilterType
                    }
                }
            )
        }
    }

    override suspend fun getWorkout(id: WorkoutId): Result<WorkoutItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutVideo(id: WorkoutId): Result<WorkoutVideo> = withContext(Dispatchers.IO) {
        runCatching {
            service.getWorkoutVideo(id).toWorkoutVideo()
        }
    }
}
