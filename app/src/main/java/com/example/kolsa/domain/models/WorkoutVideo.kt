package com.example.kolsa.domain.models

data class WorkoutVideo(
    val id: Int = -1,
    val duration: String = "",
    val link: String = "",
    val qualityList: List<Quality> = emptyList(),
)
