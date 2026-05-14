package com.nammavastra.app

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class NammaVastraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Enable Firestore offline persistence
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
            
        try {
            val db = FirebaseFirestore.getInstance()
            db.firestoreSettings = settings
        } catch (e: Exception) {
            // Fails gracefully if Firebase isn't initialized locally yet
        }
    }
}
