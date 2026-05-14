package com.nammavastra.app.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nammavastra.app.R
import com.nammavastra.app.ui.components.EmptyStateView
import com.nammavastra.app.utils.NetworkMonitor
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.material.transition.MaterialElevationScale
import android.widget.ImageView

class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: GalleryAdapter
    private lateinit var toolbar: MaterialToolbar
    private lateinit var emptyStateView: EmptyStateView
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        emptyStateView = view.findViewById(R.id.empty_state_view)
        networkMonitor = NetworkMonitor(requireContext())

        setupRecyclerView(view)
        setupFab(view)
        observeViewModel()
        setupToolbar()
        
        networkMonitor.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected && viewModel.sarees.value.isNullOrEmpty()) {
                emptyStateView.showNoInternet {
                    // Retry logic
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rv_gallery)
        adapter = GalleryAdapter(
            onClick = { trend, position ->
                if (viewModel.isSelectionMode) {
                    viewModel.toggleSelection(trend.id)
                } else {
                    // Shared Element Transition for Detail Activity
                    val holder = rv.findViewHolderForAdapterPosition(position) as? GalleryAdapter.GalleryViewHolder
                    val imageView = holder?.itemView?.findViewById<ImageView>(R.id.iv_saree)
                    
                    val intent = Intent(requireContext(), SareeDetailActivity::class.java).apply {
                        putExtra("START_POSITION", position)
                        putExtra("TRANSITION_NAME", "saree_image_${trend.id}")
                    }
                    
                    if (imageView != null) {
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            requireActivity(),
                            imageView,
                            imageView.transitionName
                        )
                        startActivity(intent, options.toBundle())
                    } else {
                        startActivity(intent)
                    }
                }
            },
            onLongClick = { trend ->
                viewModel.toggleSelection(trend.id)
            }
        )
        rv.adapter = adapter
    }

    private fun setupFab(view: View) {
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_upload)
        fab.transitionName = "fab_to_upload"
        fab.setOnClickListener {
            // Setup exit transition for this fragment so it scales down
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300
            }
            
            val extras = FragmentNavigatorExtras(fab to "shared_upload_container")
            findNavController().navigate(R.id.nav_upload, null, null, extras)
        }
    }

    private fun setupToolbar() {
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_delete) {
                viewModel.deleteSelected()
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.sarees.observe(viewLifecycleOwner) { sarees ->
            adapter.submitList(sarees)
            if (sarees.isNullOrEmpty()) {
                emptyStateView.showEmptyData("Your gallery is empty.\nTap + to upload your first saree!")
            } else {
                emptyStateView.hide()
            }
        }

        viewModel.selectedIds.observe(viewLifecycleOwner) { selected ->
            adapter.updateSelection(selected)
            if (selected.isNotEmpty()) {
                toolbar.title = "${selected.size} Selected"
                if (toolbar.menu.findItem(R.id.action_delete) == null) {
                    toolbar.inflateMenu(R.menu.menu_gallery_selection)
                }
            } else {
                toolbar.title = "My Gallery"
                toolbar.menu.clear()
            }
        }
    }
}
