package com.example.kolsa.di

import com.example.kolsa.presentation.workout.workout_detail.WorkoutDetailViewModel
import com.example.kolsa.presentation.workout.workout_list.WorkoutListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val presentationModules = module {
    viewModelOf(::WorkoutListViewModel)
    viewModelOf(::WorkoutDetailViewModel)
}

val koinAppModules =
    presentationModules
