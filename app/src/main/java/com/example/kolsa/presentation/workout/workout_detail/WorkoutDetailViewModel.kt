package com.example.kolsa.presentation.workout.workout_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kolsa.domain.models.WorkoutId

class WorkoutDetailViewModel(
    args: WorkoutDetailFragmentArgs,
) : ViewModel() {
    private val _workoutId: MutableLiveData<WorkoutId> = MutableLiveData(args.workoutId)
    val workoutId: LiveData<WorkoutId> = _workoutId
}
