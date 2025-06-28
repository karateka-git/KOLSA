package com.example.kolsa.domain.models


data class WorkoutItem(
    val id: WorkoutId,
    val title: String,
    val description: String? = null,
    val type: WorkoutType,
    val duration: String,
)

enum class WorkoutType {
    Training,
    Broadcast,
    Complex,
    Unknown;

    companion object {
        fun getWorkoutType(value: Int?) =
            when (value) {
                1 -> Training
                2 -> Broadcast
                3 -> Complex
                else -> Unknown
            }
    }
}
