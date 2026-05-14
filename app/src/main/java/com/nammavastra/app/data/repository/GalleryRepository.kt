package com.nammavastra.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nammavastra.app.data.model.Trend
import kotlinx.coroutines.tasks.await

class GalleryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "trends" // Using trends collection as discussed

    suspend fun getUserSarees(userId: String): List<Trend> {
        return try {
            // In a real app we'd filter by userId: .whereEqualTo("userId", userId)
            // But for our demo mock, we just fetch all or use mocks.
            val snapshot = db.collection(collectionName).get().await()
            val sarees = snapshot.toObjects(Trend::class.java)
            if (sarees.isEmpty()) getMockGallery() else sarees
        } catch (e: Exception) {
            getMockGallery()
        }
    }

    suspend fun deleteSarees(sareeIds: List<String>): Result<Unit> {
        return try {
            val batch = db.batch()
            for (id in sareeIds) {
                val ref = db.collection(collectionName).document(id)
                batch.delete(ref)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMockGallery(): List<Trend> {
        return listOf(
            Trend("1", "Kanjivaram Blue", "https://images.unsplash.com/photo-1610189014164-85012354c0e0?q=80&w=500", "Silk"),
            Trend("2", "Cotton Block Print", "https://images.unsplash.com/photo-1583391733959-b0eb97bf5d04?q=80&w=500", "Cotton"),
            Trend("3", "Mysore Crepe", "https://images.unsplash.com/photo-1601309503468-b7c126db0f93?q=80&w=500", "Crepe"),
            Trend("4", "Gadwal Silk", "https://images.unsplash.com/photo-1589465885857-44edb59bbff2?q=80&w=500", "Silk"),
            Trend("5", "Golden Tissue", "https://images.unsplash.com/photo-1583391733975-276d4db7a956?q=80&w=600", "Silk"),
            Trend("6", "Red Banarasi", "https://images.unsplash.com/photo-1601309503468-b7c126db0f93?q=80&w=800", "Banarasi")
        )
    }
}
