package com.example.kolsa.presentation.workout.workout_list.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.kolsa.databinding.ViewHolderWorkoutItemBinding
import com.example.kolsa.domain.models.WorkoutItem

class WorkoutItemViewHolder(
    private val binding: ViewHolderWorkoutItemBinding,
    private val onItemClick: (WorkoutItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: WorkoutItem? = null

    init {
        binding.apply {
            root.setOnClickListener {
                currentItem?.let {
                    onItemClick(it)
                }
            }
        }
    }

    fun bindTo(item: WorkoutItem) {
        currentItem = item
        binding.apply {
            title.text = item.title
        }
    }
}
