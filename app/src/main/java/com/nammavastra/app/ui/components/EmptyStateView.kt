package com.nammavastra.app.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.nammavastra.app.R

class EmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val ivIllustration: ImageView
    private val tvMessage: TextView
    private val btnRetry: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.view_empty_state, this, true)
        ivIllustration = findViewById(R.id.iv_illustration)
        tvMessage = findViewById(R.id.tv_message)
        btnRetry = findViewById(R.id.btn_retry)
        
        // Default hidden
        visibility = GONE
    }

    fun showNoInternet(onRetry: () -> Unit) {
        visibility = VISIBLE
        ivIllustration.setImageResource(android.R.drawable.ic_dialog_alert) // fallback system icon
        tvMessage.text = "You're offline and no cached data is available."
        btnRetry.visibility = VISIBLE
        btnRetry.setOnClickListener { onRetry() }
    }

    fun showEmptyData(message: String = "No data found", onAction: (() -> Unit)? = null) {
        visibility = VISIBLE
        ivIllustration.setImageResource(android.R.drawable.ic_menu_search) // fallback system icon
        tvMessage.text = message
        if (onAction != null) {
            btnRetry.visibility = VISIBLE
            btnRetry.text = "Refresh"
            btnRetry.setOnClickListener { onAction() }
        } else {
            btnRetry.visibility = GONE
        }
    }

    fun showError(message: String, onRetry: () -> Unit) {
        visibility = VISIBLE
        ivIllustration.setImageResource(android.R.drawable.stat_notify_error)
        tvMessage.text = message
        btnRetry.visibility = VISIBLE
        btnRetry.text = "Try Again"
        btnRetry.setOnClickListener { onRetry() }
    }

    fun hide() {
        visibility = GONE
    }
}
