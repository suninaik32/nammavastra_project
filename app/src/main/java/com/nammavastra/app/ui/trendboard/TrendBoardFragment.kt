package com.nammavastra.app.ui.trendboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.nammavastra.app.R

import com.nammavastra.app.ui.components.EmptyStateView
import com.nammavastra.app.utils.NetworkMonitor

class TrendBoardFragment : Fragment() {

    private val viewModel: TrendBoardViewModel by viewModels()
    private lateinit var adapter: TrendBoardAdapter
    private lateinit var emptyStateView: EmptyStateView
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trend_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyStateView = view.findViewById(R.id.empty_state_view)
        networkMonitor = NetworkMonitor(requireContext())

        setupRecyclerView(view)
        setupChips(view)
        setupSearch(view)
        observeViewModel()
        
        networkMonitor.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected && viewModel.displayedTrends.value.isNullOrEmpty()) {
                emptyStateView.showNoInternet {
                    // Retry logic
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_trends)
        // Set Staggered Grid with 2 columns
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        
        adapter = TrendBoardAdapter { trend ->
            viewModel.toggleSave(trend.id)
        }
        recyclerView.adapter = adapter
    }

    private fun setupChips(view: View) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group_filters)
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChip = view.findViewById<Chip>(checkedIds.first())
                viewModel.setCategoryFilter(selectedChip.text.toString())
            } else {
                // Default to All if somehow deselected
                viewModel.setCategoryFilter("All")
                view.findViewById<Chip>(R.id.chip_all)?.isChecked = true
            }
        }
    }

    private fun setupSearch(view: View) {
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        
        searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.displayedTrends.observe(viewLifecycleOwner) { trends ->
            adapter.submitList(trends)
            if (trends.isNullOrEmpty()) {
                emptyStateView.showEmptyData("No trends found matching your filter") {
                    viewModel.setCategoryFilter("All")
                }
            } else {
                emptyStateView.hide()
            }
        }
    }
}
