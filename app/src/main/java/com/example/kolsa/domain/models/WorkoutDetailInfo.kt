package com.example.kolsa.domain.models

data class WorkoutDetailInfo(
    val id: WorkoutId = WorkoutId(),
    val video: WorkoutVideo = WorkoutVideo(),
)
