package com.nammavastra.app.ui.trendboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammavastra.app.data.model.Trend
import com.nammavastra.app.data.repository.TrendRepository
import kotlinx.coroutines.launch

class TrendBoardViewModel : ViewModel() {
    private val repository = TrendRepository()

    private val _allTrends = MutableLiveData<List<Trend>>()
    
    private val _displayedTrends = MutableLiveData<List<Trend>>()
    val displayedTrends: LiveData<List<Trend>> = _displayedTrends

    private var currentCategory = "All"
    private var currentQuery = ""

    init {
        fetchTrends()
    }

    private fun fetchTrends() {
        viewModelScope.launch {
            val trends = repository.getTrends()
            _allTrends.value = trends
            applyFilters()
        }
    }

    fun setCategoryFilter(category: String) {
        currentCategory = category
        applyFilters()
    }

    fun setSearchQuery(query: String) {
        currentQuery = query
        applyFilters()
    }

    fun toggleSave(trendId: String) {
        // In a real app, you'd also update this in Firestore via repository
        val updatedList = _allTrends.value?.map {
            if (it.id == trendId) it.copy(isSaved = !it.isSaved) else it
        }
        _allTrends.value = updatedList ?: emptyList()
        applyFilters()
    }

    private fun applyFilters() {
        val all = _allTrends.value ?: return
        
        var filtered = if (currentCategory == "All") {
            all
        } else {
            all.filter { it.category.equals(currentCategory, ignoreCase = true) }
        }

        if (currentQuery.isNotBlank()) {
            filtered = filtered.filter { 
                it.title.contains(currentQuery, ignoreCase = true) || 
                it.category.contains(currentQuery, ignoreCase = true) 
            }
        }

        _displayedTrends.value = filtered
    }
}
