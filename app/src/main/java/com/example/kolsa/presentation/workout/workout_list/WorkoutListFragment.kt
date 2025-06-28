package com.example.kolsa.presentation.workout.workout_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kolsa.databinding.FragmentWorkoutListBinding
import com.example.kolsa.presentation.util.ext.onDestroyNullable


class WorkoutListFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentWorkoutListBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutListBinding.inflate(layoutInflater, container, false)
        return binding.root.also {
            it.setOnClickListener {
                findNavController().navigate(WorkoutListFragmentDirections.actionWorkoutListFragmentToWorkoutDetailFragment())
            }
        }
    }
}
