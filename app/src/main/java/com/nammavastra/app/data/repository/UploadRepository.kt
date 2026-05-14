package com.nammavastra.app.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nammavastra.app.data.model.Trend
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UploadRepository {
    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun uploadSaree(
        uri: Uri,
        title: String,
        material: String,
        category: String,
        price: Double,
        onProgress: (Float) -> Unit
    ): Result<Unit> {
        return try {
            // 1. Upload Image
            val userId = "user_123" // In a real app, get from FirebaseAuth
            val timestamp = System.currentTimeMillis()
            val fileName = "sarees/$userId/$timestamp.jpg"
            val storageRef = storage.reference.child(fileName)

            val uploadTask = storageRef.putFile(uri)
            
            // We use a listener for progress, but await the final result
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                onProgress(progress.toFloat())
            }

            uploadTask.await()
            val downloadUrl = storageRef.downloadUrl.await()

            // 2. Save Metadata to Firestore
            val sareeData = Trend(
                id = UUID.randomUUID().toString(),
                title = title,
                imageUrl = downloadUrl.toString(),
                category = category,
                badge = "New",
                isSaved = false
            )
            
            // Using a map to append extra fields like material and price easily
            val dataMap = hashMapOf(
                "id" to sareeData.id,
                "title" to sareeData.title,
                "imageUrl" to sareeData.imageUrl,
                "category" to sareeData.category,
                "material" to material,
                "price" to price,
                "badge" to sareeData.badge,
                "isSaved" to sareeData.isSaved,
                "uploadedAt" to timestamp
            )

            db.collection("trends").document(sareeData.id).set(dataMap).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
