package com.example.kolsa.data.models.response

import com.example.kolsa.domain.models.WorkoutId
import com.example.kolsa.domain.models.WorkoutItem
import com.example.kolsa.domain.models.WorkoutType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutItemResponse(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val type: Int? = null,
    val duration: String? = null,
)

fun WorkoutItemResponse.toWorkoutItem() = WorkoutItem(
    id = WorkoutId(id ?: -1),
    title = title.orEmpty(),
    description = description,
    type = WorkoutType.getWorkoutType(type),
    duration = duration.orEmpty(),
)
