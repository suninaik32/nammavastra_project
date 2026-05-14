package com.nammavastra.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nammavastra.app.data.model.Saree

data class StatMetric(val title: String, val value: String, val iconResId: Int)

data class FeaturedTrend(val title: String, val imageUrl: String)

class HomeViewModel : ViewModel() {

    private val _weaverName = MutableLiveData<String>("Lakshmi Silk Weavers")
    val weaverName: LiveData<String> = _weaverName

    private val _profileImageUrl = MutableLiveData<String>("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=200&auto=format&fit=crop") // Placeholder portrait
    val profileImageUrl: LiveData<String> = _profileImageUrl

    private val _stats = MutableLiveData<List<StatMetric>>()
    val stats: LiveData<List<StatMetric>> = _stats

    private val _featuredTrend = MutableLiveData<FeaturedTrend>()
    val featuredTrend: LiveData<FeaturedTrend> = _featuredTrend

    private val _recentUploads = MutableLiveData<List<Saree>>()
    val recentUploads: LiveData<List<Saree>> = _recentUploads

    init {
        loadMockData()
    }

    private fun loadMockData() {
        // Mock Stats (Assume we have some default icons, using android.R.drawable placeholders or custom ones later)
        _stats.value = listOf(
            StatMetric("Total Sarees", "124", android.R.drawable.ic_menu_gallery),
            StatMetric("Avg Price", "₹4,500", android.R.drawable.ic_menu_sort_by_size),
            StatMetric("Top Category", "Silk", android.R.drawable.ic_menu_view)
        )

        // Mock Featured Trend
        _featuredTrend.value = FeaturedTrend(
            "Festive Banarasi Collection",
            "https://images.unsplash.com/photo-1610189013688-662eb7a1ea91?q=80&w=800&auto=format&fit=crop" // beautiful saree image
        )

        // Mock Recent Uploads
        _recentUploads.value = listOf(
            Saree("1", "Kanjivaram Blue", "https://images.unsplash.com/photo-1610189014164-85012354c0e0?q=80&w=500&auto=format&fit=crop", "Silk", "Pure Silk", 5500.0),
            Saree("2", "Cotton Block Print", "https://images.unsplash.com/photo-1583391733959-b0eb97bf5d04?q=80&w=500&auto=format&fit=crop", "Cotton", "Cotton", 1200.0),
            Saree("3", "Mysore Crepe", "https://images.unsplash.com/photo-1601309503468-b7c126db0f93?q=80&w=500&auto=format&fit=crop", "Crepe", "Silk Blend", 3200.0),
            Saree("4", "Gadwal Silk", "https://images.unsplash.com/photo-1589465885857-44edb59bbff2?q=80&w=500&auto=format&fit=crop", "Silk", "Silk", 4800.0)
        )
    }
}
