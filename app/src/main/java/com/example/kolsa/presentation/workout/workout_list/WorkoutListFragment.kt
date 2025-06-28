package com.example.kolsa.presentation.workout.workout_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kolsa.databinding.FragmentWorkoutListBinding
import com.example.kolsa.domain.models.WorkoutItem
import com.example.kolsa.domain.models.WorkoutType
import com.example.kolsa.presentation.util.ext.getStringRes
import com.example.kolsa.presentation.util.ext.onDestroyNullable
import com.example.kolsa.presentation.workout.workout_list.adapters.WorkoutAdapter
import com.example.kolsa.presentation.workout.workout_list.adapters.WorkoutItemViewHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkoutListFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentWorkoutListBinding>()
    private val viewModel: WorkoutListViewModel by viewModel()

    private val workoutFilterTypePopupMenu by lazy {
        PopupMenu(this.requireContext(), binding.workoutTypeFilterButton).apply {
            WorkoutType.entries.forEach { type ->
                menu.add(Menu.NONE, type.ordinal, type.ordinal, type.getStringRes())
                    .setOnMenuItemClickListener {
                        viewModel.onChangeSelectedType(type)
                        true
                    }
            }
        }
    }

    private val workoutListAdapter by lazy {
        WorkoutAdapter {
            WorkoutItemViewHolder(
                it,
                ::navigateToDetail
            )
        }
    }

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
        ViewCompat.setOnApplyWindowInsetsListener(binding.workoutListContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        // TODO можно вынести в базовый фрагмент
        initRecycler()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.workoutTypeFilterButton.setOnClickListener {
            workoutFilterTypePopupMenu.show()
        }
    }

    private fun initRecycler() {
        binding.apply {
            workoutListRecycler.adapter = workoutListAdapter
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { uiState ->
                    binding.apply {
                        when {
                            uiState.error != null -> {
                                loadingProgressBar.isVisible = false
                                workoutListContainer.isVisible = false
                                errorTextView.isVisible = true
                                updatingProgressBar.isVisible = false
                            }
                            uiState.isLoading -> {
                                loadingProgressBar.isVisible = true
                                workoutListContainer.isVisible = false
                                errorTextView.isVisible = false
                                updatingProgressBar.isVisible = false
                            }
                            uiState.isUpdating -> {
                                updatingProgressBar.isVisible = true
                            }
                            else -> {
                                loadingProgressBar.isVisible = false
                                workoutListContainer.isVisible = true
                                errorTextView.isVisible = false
                                updatingProgressBar.isVisible = false
                                workoutTypeFilterButton.text = requireContext().getText(uiState.selectedFilterType.getStringRes())
                                workoutListAdapter.swapItems(uiState.workoutList.items)
                            }
                        }
                    }
                }
        }
    }

    private fun navigateToDetail(workoutItem: WorkoutItem) {
        findNavController().navigate(
            WorkoutListFragmentDirections.actionWorkoutListFragmentToWorkoutDetailFragment(workoutItem.id)
        )
    }
}
