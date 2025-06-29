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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class WorkoutRepositoryImpl(
    private val service: WorkoutApi
) : WorkoutRepository, KoinComponent {
    private var cashedWorkoutList: WorkoutList? = null

    override suspend fun getWorkoutList(
        searchQuery: String,
        selectedFilterType: WorkoutType,
    ): Result<WorkoutList> = withContext(Dispatchers.IO) {
        runCatching {
            val items = cashedWorkoutList?.items
                ?: service.getWorkoutList().map { it.toWorkoutItem() }.also {
                    cashedWorkoutList = WorkoutList(items = it)
                }

            WorkoutList(
                items = (items+items+items).asSequence()
                    .filter { it.title.contains(searchQuery, true) }
                    .filter {
                        when(selectedFilterType) {
                            WorkoutType.Unknown -> true
                            else -> it.type == selectedFilterType
                        }
                    }.toList()
            )
        }
    }

    override suspend fun getWorkout(id: WorkoutId): Result<WorkoutItem> = withContext(Dispatchers.IO) {
        runCatching {
            cashedWorkoutList?.items?.first {
                it.id == id
            } ?: throw Exception("cashedWorkoutList is not initialize")
        }
    }

    override suspend fun getWorkoutVideo(id: WorkoutId): Result<WorkoutVideo> = withContext(Dispatchers.IO) {
        runCatching {
            service.getWorkoutVideo(id).toWorkoutVideo()
        }
    }
}
