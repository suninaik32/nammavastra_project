package com.nammavastra.app.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammavastra.app.data.model.Trend
import com.nammavastra.app.data.repository.GalleryRepository
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {
    private val repository = GalleryRepository()

    private val _sarees = MutableLiveData<List<Trend>>()
    val sarees: LiveData<List<Trend>> = _sarees

    private val _selectedIds = MutableLiveData<Set<String>>(emptySet())
    val selectedIds: LiveData<Set<String>> = _selectedIds

    val isSelectionMode: Boolean
        get() = _selectedIds.value?.isNotEmpty() == true

    init {
        loadGallery()
    }

    private fun loadGallery() {
        viewModelScope.launch {
            val items = repository.getUserSarees("user_123")
            _sarees.value = items
        }
    }

    fun toggleSelection(id: String) {
        val current = _selectedIds.value?.toMutableSet() ?: mutableSetOf()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _selectedIds.value = current
    }

    fun clearSelection() {
        _selectedIds.value = emptySet()
    }

    fun deleteSelected() {
        val idsToDelete = _selectedIds.value?.toList() ?: return
        if (idsToDelete.isEmpty()) return

        viewModelScope.launch {
            repository.deleteSarees(idsToDelete)
            // Update local state even if mock for UI reflect
            val currentSarees = _sarees.value?.toMutableList() ?: mutableListOf()
            currentSarees.removeAll { it.id in idsToDelete }
            _sarees.value = currentSarees
            clearSelection()
        }
    }
}
