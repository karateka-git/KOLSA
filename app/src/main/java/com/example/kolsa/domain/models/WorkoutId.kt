package com.example.kolsa.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@JvmInline
@Parcelize
value class WorkoutId(val id: Int = -1): Parcelable
