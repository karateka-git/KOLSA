package com.example.kolsa.presentation.workout.workout_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolsa.domain.models.WorkoutList
import com.example.kolsa.domain.models.WorkoutType
import com.example.kolsa.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

data class WorkoutListState(
    val isLoading: Boolean = true,
    val isUpdating: Boolean = false,
    val error: Throwable? = null,
    val selectedFilterType: WorkoutType = WorkoutType.Unknown,
    val searchQuery: String = "",
    val workoutList: WorkoutList = WorkoutList(),
)

class WorkoutListViewModel(
    private val repository: WorkoutRepository
) : ViewModel(), KoinComponent {
    private val _uiState: MutableStateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())
    val uiState: StateFlow<WorkoutListState> = _uiState

    init {
        viewModelScope.launch {
            val result = repository.getWorkoutList()
            result.fold(
                onSuccess = {
                    _uiState.emit(_uiState.value.copy(
                        isLoading = false,
                        workoutList = it,
                        error = null,
                    ))
                },
                onFailure = {
                    _uiState.emit(_uiState.value.copy(isLoading = false, error = it))
                }
            )
        }
    }

    fun onChangeSelectedType(type: WorkoutType) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    isUpdating = true,
                )
            )

            repository.getWorkoutList(
                searchQuery = _uiState.value.searchQuery,
                selectedFilterType = type,
            ).fold(
                onSuccess = {
                    _uiState.emit(
                        _uiState.value.copy(
                            selectedFilterType = type,
                            workoutList = it,
                            isUpdating = false,
                        )
                    )
                },
                onFailure = {
                    _uiState.emit(
                        _uiState.value.copy(
                            error = it,
                            isUpdating = false,
                        )
                    )
                }
            )
        }
    }

    fun onSearchQueryChanged(searchText: String) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    searchQuery = searchText,
                )
            )

            repository.getWorkoutList(
                searchQuery = searchText,
                selectedFilterType = _uiState.value.selectedFilterType,
            ).fold(
                onSuccess = {
                    _uiState.emit(
                        _uiState.value.copy(
                            workoutList = it,
                        )
                    )
                },
                onFailure = {
                    _uiState.emit(
                        _uiState.value.copy(
                            error = it,
                        )
                    )
                }
            )
        }
    }
}
