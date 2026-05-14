package com.nammavastra.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nammavastra.app.R
import com.nammavastra.app.ui.components.EmptyStateView
import com.nammavastra.app.utils.NetworkMonitor

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var statsAdapter: StatsAdapter
    private lateinit var recentUploadsAdapter: RecentUploadsAdapter
    private lateinit var emptyStateView: EmptyStateView
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyStateView = view.findViewById(R.id.empty_state_view)
        networkMonitor = NetworkMonitor(requireContext())

        setupAdapters(view)
        observeViewModel()
        
        networkMonitor.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected && viewModel.stats.value.isNullOrEmpty() && viewModel.recentUploads.value.isNullOrEmpty()) {
                emptyStateView.showNoInternet {
                    // Retry logic (viewModel.fetchData())
                }
            }
        }
    }

    private fun setupAdapters(view: View) {
        statsAdapter = StatsAdapter()
        view.findViewById<RecyclerView>(R.id.rv_stats).adapter = statsAdapter

        recentUploadsAdapter = RecentUploadsAdapter()
        view.findViewById<RecyclerView>(R.id.rv_recent_uploads).adapter = recentUploadsAdapter
    }

    private fun observeViewModel() {
        // Setup Observers
        viewModel.weaverName.observe(viewLifecycleOwner) { name ->
            view?.findViewById<TextView>(R.id.tv_weaver_name)?.text = name
        }

        viewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            val ivProfile = view?.findViewById<ImageView>(R.id.iv_profile)
            if (ivProfile != null) {
                ivProfile.load(url)
            }
        }

        viewModel.stats.observe(viewLifecycleOwner) { statsList ->
            statsAdapter.submitList(statsList)
            checkEmptyState()
        }

        viewModel.featuredTrend.observe(viewLifecycleOwner) { featured ->
            featured?.let {
                view?.findViewById<TextView>(R.id.tv_featured_trend_title)?.text = it.title
                val ivFeatured = view?.findViewById<ImageView>(R.id.iv_featured_trend)
                if (ivFeatured != null) {
                    ivFeatured.load(it.imageUrl)
                }
            }
        }

        viewModel.recentUploads.observe(viewLifecycleOwner) { recentList ->
            recentUploadsAdapter.submitList(recentList)
            checkEmptyState()
        }
    }

    private fun checkEmptyState() {
        val statsEmpty = viewModel.stats.value.isNullOrEmpty()
        val recentEmpty = viewModel.recentUploads.value.isNullOrEmpty()
        
        if (statsEmpty && recentEmpty) {
            emptyStateView.showEmptyData("No dashboard data available") {
                // Retry
            }
        } else {
            emptyStateView.hide()
        }
    }
}
