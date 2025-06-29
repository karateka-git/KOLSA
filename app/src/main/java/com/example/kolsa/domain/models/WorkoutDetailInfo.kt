package com.example.kolsa.domain.models

data class WorkoutDetailInfo(
    val id: WorkoutId = WorkoutId(),
    val info: WorkoutItem = WorkoutItem(),
    val video: WorkoutVideo = WorkoutVideo(),
)
