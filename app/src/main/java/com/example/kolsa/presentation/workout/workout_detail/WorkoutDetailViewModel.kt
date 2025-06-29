package com.example.kolsa.presentation.workout.workout_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C.TRACK_TYPE_VIDEO
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import com.example.kolsa.domain.models.Quality
import com.example.kolsa.domain.models.WorkoutDetailInfo
import com.example.kolsa.domain.repositories.WorkoutRepository
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WorkoutDetailState(
    val isLoading: Boolean = true,
    val isUpdating: Boolean = false,
    val error: Throwable? = null,
    val workoutDetailInfo: WorkoutDetailInfo = WorkoutDetailInfo(),
)

class WorkoutDetailViewModel(
    args: WorkoutDetailFragmentArgs,
    private val repository: WorkoutRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<WorkoutDetailState> = MutableStateFlow(WorkoutDetailState())
    val uiState: StateFlow<WorkoutDetailState> = _uiState

    val videoLink: StateFlow<String?> = _uiState.map { it.workoutDetailInfo.video.link }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val qualityList: StateFlow<List<Quality>> = _uiState.map { it.workoutDetailInfo.video.qualityList }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            val result = repository.getWorkoutVideo(args.workoutId)
            result.fold(
                onSuccess = {
                    _uiState.emit(_uiState.value.copy(
                        isLoading = false,
                        workoutDetailInfo = WorkoutDetailInfo(id = args.workoutId, video = it),
                        error = null,
                    ))
                },
                onFailure = {
                    _uiState.emit(_uiState.value.copy(isLoading = false, error = it))
                }
            )
        }
    }


    fun generateQualityList(
        groups: ImmutableList<Tracks.Group>,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val qualityList: MutableList<Quality> = mutableListOf()

            groups.find {
                it.length != 0 && it.type == TRACK_TYPE_VIDEO
            }?.let { videoGroup ->
                for (i in 0 until videoGroup.length) {
                    if (videoGroup.isTrackSupported(i)) {
                        val track = videoGroup.getTrackFormat(i)
                        val trackName = "${track.width}p"
                        val trackSelectionOverride = TrackSelectionOverride(
                            videoGroup.mediaTrackGroup,
                            i
                        )
                        val trackIsSelected = videoGroup.isTrackSelected(i)
                        qualityList.add(Quality(trackName, trackSelectionOverride, trackIsSelected))
                    }
                }
                val autoQuality = if (qualityList.all { it.isSelected }) {
                    qualityList.forEach { it.isSelected = false }
                    true
                } else {
                    false
                }
                qualityList.add(
                    Quality(
                        "Auto",
                        TrackSelectionOverride(
                            videoGroup.mediaTrackGroup,
                            (0 until videoGroup.length).toList()
                        ),
                        autoQuality
                    )
                )
            }

            with(_uiState.value) {
                _uiState.emit(
                    copy(
                        isLoading = false,
                        workoutDetailInfo = workoutDetailInfo.copy(
                            video = workoutDetailInfo.video.copy(
                                qualityList = qualityList
                            )
                        ),
                        error = null,
                    )
                )
            }
        }
    }

    fun updateSelectedQuality(newSelectedQuality: Quality) {
        viewModelScope.launch {
            with(_uiState.value) {
                _uiState.emit(
                    copy(
                        isLoading = false,
                        workoutDetailInfo = workoutDetailInfo.copy(
                            video = workoutDetailInfo.video.copy(
                                qualityList = workoutDetailInfo.video.qualityList.map {
                                    it.copy(
                                        isSelected = it.name == newSelectedQuality.name
                                    )
                                }
                            )
                        ),
                        error = null,
                    )
                )
            }
        }
    }
}
