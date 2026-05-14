package com.nammavastra.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CalculatorRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun saveCalculation(
        category: String,
        materialCost: Double,
        profitMargin: Int,
        finalPrice: Double
    ): Result<Unit> {
        return try {
            val calculationId = UUID.randomUUID().toString()
            val userId = "user_123" // Hardcoded for demo
            
            val dataMap = hashMapOf(
                "id" to calculationId,
                "userId" to userId,
                "category" to category,
                "materialCost" to materialCost,
                "profitMargin" to profitMargin,
                "finalPrice" to finalPrice,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("calculations").document(calculationId).set(dataMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
