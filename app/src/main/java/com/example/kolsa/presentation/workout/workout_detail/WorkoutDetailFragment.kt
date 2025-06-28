package com.example.kolsa.presentation.workout.workout_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kolsa.databinding.FragmentWorkoutDetailBinding
import com.example.kolsa.presentation.util.ext.onDestroyNullable


class WorkoutDetailFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentWorkoutDetailBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}
