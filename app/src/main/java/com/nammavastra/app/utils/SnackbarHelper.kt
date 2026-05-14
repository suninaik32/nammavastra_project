package com.nammavastra.app.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nammavastra.app.R

object SnackbarHelper {
    fun showOfflineSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "You are offline — showing cached data", Snackbar.LENGTH_INDEFINITE)
        
        // Set background color to error color (usually a red variant) or warning color
        snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.holo_red_dark))
        snackbar.setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
        
        // Add a dismiss action
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, android.R.color.white))
        
        snackbar.show()
    }
    
    fun showOnlineSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "Back online", Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))
        snackbar.setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
        snackbar.show()
    }
}
