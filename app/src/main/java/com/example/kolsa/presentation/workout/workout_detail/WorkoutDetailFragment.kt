package com.example.kolsa.presentation.workout.workout_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kolsa.databinding.FragmentWorkoutDetailBinding
import com.example.kolsa.presentation.util.ext.onDestroyNullable
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class WorkoutDetailFragment : Fragment() {
    private var binding by onDestroyNullable<FragmentWorkoutDetailBinding>()
    private val args by navArgs<WorkoutDetailFragmentArgs>()
    private val viewModel: WorkoutDetailViewModel by viewModel(
        parameters = { parametersOf(args) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO можно вынести в базовый фрагмент
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.root.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObservers() {
        viewModel.workoutId.observe(viewLifecycleOwner) {
            binding.textview.text = "${it.id}"
        }
    }
}
