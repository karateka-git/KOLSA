package com.example.kolsa.presentation.workout.workout_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutListViewModel() : ViewModel() {
    private val _searchQuery: MutableLiveData<String> = MutableLiveData("Workout list init")
    val searchQuery: LiveData<String> = _searchQuery

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
