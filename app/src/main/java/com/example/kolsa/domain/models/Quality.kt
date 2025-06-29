package com.example.kolsa.domain.models

import androidx.media3.common.TrackSelectionOverride

data class Quality(
    val name: String,
    val selectionOverride: TrackSelectionOverride,
    var isSelected: Boolean = false
)
