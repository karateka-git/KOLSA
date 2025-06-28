package com.example.kolsa.presentation.workout.workout_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kolsa.databinding.FragmentWorkoutListBinding
import com.example.kolsa.presentation.util.ext.onDestroyNullable
import com.example.kolsa.presentation.workout.models.WorkoutId
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkoutListFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentWorkoutListBinding>()
    private val viewModel: WorkoutListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutListBinding.inflate(layoutInflater, container, false)
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
                findNavController().navigate(WorkoutListFragmentDirections.actionWorkoutListFragmentToWorkoutDetailFragment(WorkoutId(17)))
            }
        }
    }

    private fun initObservers() {
        viewModel.searchQuery.observe(viewLifecycleOwner) {
            binding.text.text = it
        }
    }
}
