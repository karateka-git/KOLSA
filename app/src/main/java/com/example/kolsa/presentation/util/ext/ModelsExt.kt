package com.example.kolsa.presentation.util.ext

import androidx.annotation.StringRes
import com.example.kolsa.R
import com.example.kolsa.domain.models.WorkoutType

@StringRes
fun WorkoutType.getStringRes(): Int {
    return when(this) {
        WorkoutType.Training -> R.string.workout_type_training
        WorkoutType.Broadcast -> R.string.workout_type_broadcast
        WorkoutType.Complex -> R.string.workout_type_complex
        WorkoutType.Unknown -> R.string.select_type_unknown
    }
}
