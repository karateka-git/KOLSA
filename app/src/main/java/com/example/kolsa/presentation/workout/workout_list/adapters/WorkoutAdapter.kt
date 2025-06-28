package com.example.kolsa.presentation.workout.workout_list.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kolsa.databinding.ViewHolderWorkoutItemBinding
import com.example.kolsa.domain.models.WorkoutItem

class WorkoutAdapter(
    private val createViewHolder: (ViewHolderWorkoutItemBinding) -> WorkoutItemViewHolder
) : RecyclerView.Adapter<WorkoutItemViewHolder>() {

    private val differ = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<WorkoutItem>() {
            override fun areItemsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
                return oldItem === newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
                return oldItem == newItem
            }
        }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutItemViewHolder =
        createViewHolder(ViewHolderWorkoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorkoutItemViewHolder, position: Int) {
        holder.bindTo(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun swapItems(newItems: List<WorkoutItem>) {
        differ.submitList(newItems)
    }
}
