package com.nammavastra.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nammavastra.app.data.model.Trend
import kotlinx.coroutines.tasks.await
import android.util.Log

class TrendRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "trends"

    suspend fun getTrends(): List<Trend> {
        return try {
            val snapshot = db.collection(collectionName).get().await()
            val trends = snapshot.toObjects(Trend::class.java)
            if (trends.isEmpty()) getMockTrends() else trends
        } catch (e: Exception) {
            Log.e("TrendRepository", "Error fetching from Firestore, falling back to mock data: ${e.message}")
            getMockTrends()
        }
    }

    private fun getMockTrends(): List<Trend> {
        return listOf(
            Trend("1", "Royal Blue Kanjivaram", "https://images.unsplash.com/photo-1589465885857-44edb59bbff2?q=80&w=600", "Kanjivaram", "Trending", true),
            Trend("2", "Pastel Silk Block Print", "https://images.unsplash.com/photo-1610189013688-662eb7a1ea91?q=80&w=400", "Silk", "New", false),
            Trend("3", "Classic Red Banarasi", "https://images.unsplash.com/photo-1601309503468-b7c126db0f93?q=80&w=800", "Banarasi", "Classic", false),
            Trend("4", "Yellow Chanderi Handloom", "https://images.unsplash.com/photo-1610189014164-85012354c0e0?q=80&w=500", "Chanderi", "New", true),
            Trend("5", "Pure Cotton Everyday", "https://images.unsplash.com/photo-1583391733959-b0eb97bf5d04?q=80&w=700", "Cotton", "Trending", false),
            Trend("6", "Golden Tissue Silk", "https://images.unsplash.com/photo-1583391733975-276d4db7a956?q=80&w=600", "Silk", "Classic", false)
        )
    }
}
