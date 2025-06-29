package com.example.kolsa.data.models.response

import com.example.kolsa.BuildConfig
import com.example.kolsa.domain.models.WorkoutVideo
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutVideoResponse(
    val id: Int? = null,
    val duration: String? = null,
    val link: String? = null,
)

fun WorkoutVideoResponse.toWorkoutVideo() =
    WorkoutVideo(
        id = id ?: -1,
        duration = duration.orEmpty(),
        link = BuildConfig.BASE_DOMAIN + link.orEmpty(),
    )
