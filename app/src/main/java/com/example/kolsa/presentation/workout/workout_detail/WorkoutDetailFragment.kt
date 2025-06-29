package com.example.kolsa.presentation.workout.workout_detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.navigation.fragment.navArgs
import com.example.kolsa.databinding.FragmentWorkoutDetailBinding
import com.example.kolsa.domain.models.Quality
import com.example.kolsa.presentation.util.ext.onDestroyNullable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@UnstableApi
class WorkoutDetailFragment : Fragment() {
    private var binding by onDestroyNullable<FragmentWorkoutDetailBinding>()
    private val args by navArgs<WorkoutDetailFragmentArgs>()
    private val viewModel: WorkoutDetailViewModel by viewModel(
        parameters = { parametersOf(args) }
    )

    private val trackSelector by lazy {
        DefaultTrackSelector(requireContext(), AdaptiveTrackSelection.Factory())
    }

    private val qualityPopupMenu by lazy {
        PopupMenu(this.requireContext(), binding.exoQuality)
    }

    private var player: ExoPlayer? = null

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
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.displayCutout() +
                        WindowInsetsCompat.Type.systemBars()
            )
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        // TODO можно вынести в базовый фрагмент
        initPlayer()
        initObservers()
        initListeners()
    }

    private fun initPlayer() {
        player = ExoPlayer
            .Builder(requireContext())
            .setTrackSelector(trackSelector)
            .build().apply {
                addListener(object : Player.Listener {
                    override fun onTracksChanged(tracks: Tracks) {
                        super.onTracksChanged(tracks)
                        viewModel.generateQualityList(currentTracks.groups)
                    }
                })
            }
    }

    private fun initListeners() {
        binding.exoQuality.setOnClickListener {
            qualityPopupMenu.show()
        }
//        binding.root.apply {
//            setOnClickListener {
//                findNavController().popBackStack()
//            }
//        }
    }

    @OptIn(UnstableApi::class)
    private fun initObservers() {
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setAllowAudioMixedMimeTypeAdaptiveness(true)
        )
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED,
            ).collectLatest { uiState ->
                with(binding) {
                    textview.text = uiState.workoutDetailInfo.id.toString()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.videoLink.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED,
            ).filterNotNull().collectLatest { link ->
                with(binding) {
                    playerView.player = player?.apply {
                        setMediaItem(
                            MediaItem.fromUri(Uri.parse(link))
                        )
                        prepare()
                        playWhenReady = true
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.qualityList.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED,
            ).collectLatest { qualityList ->
                updateQualityPopupMenu(qualityList)
            }
        }
    }

    private fun updateQualityPopupMenu(qualityList: List<Quality>) {
        binding.exoQuality.text = qualityList.firstOrNull { it.isSelected }?.name
        qualityPopupMenu.menu.clear()
        qualityList.forEachIndexed { index, (name, _, isSelected) ->
            qualityPopupMenu.menu.add(0, index, 0, name)
        }
        qualityPopupMenu.setOnMenuItemClickListener { menuItem ->
            qualityList[menuItem.itemId].let {
                viewModel.updateSelectedQuality(it)
                player?.apply {
                    trackSelectionParameters =
                        trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(it.selectionOverride)
                            .build()
                }
            }
            true
        }
    }

}
