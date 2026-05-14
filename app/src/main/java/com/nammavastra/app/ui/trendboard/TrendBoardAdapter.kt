package com.nammavastra.app.ui.trendboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.chip.Chip
import com.nammavastra.app.R
import com.nammavastra.app.data.model.Trend

class TrendBoardAdapter(
    private val onSaveClick: (Trend) -> Unit
) : ListAdapter<Trend, TrendBoardAdapter.TrendViewHolder>(TrendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trend_card, parent, false)
        return TrendViewHolder(view, onSaveClick)
    }

    override fun onBindViewHolder(holder: TrendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TrendViewHolder(itemView: View, val onSaveClick: (Trend) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_trend_image)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_trend_title)
        private val tvCategory: TextView = itemView.findViewById(R.id.tv_trend_category)
        private val chipBadge: Chip = itemView.findViewById(R.id.chip_badge)
        private val btnSave: ImageButton = itemView.findViewById(R.id.btn_save)

        fun bind(trend: Trend) {
            tvTitle.text = trend.title
            tvCategory.text = trend.category
            chipBadge.text = trend.badge
            
            // Set badge color dynamically based on text (just a simple example)
            val badgeColor = when (trend.badge.lowercase()) {
                "trending" -> itemView.context.getColor(android.R.color.holo_red_light)
                "new" -> itemView.context.getColor(android.R.color.holo_green_dark)
                else -> itemView.context.getColor(android.R.color.darker_gray)
            }
            chipBadge.setChipBackgroundColorResource(android.R.color.transparent) // reset
            chipBadge.chipBackgroundColor = android.content.res.ColorStateList.valueOf(badgeColor)

            btnSave.setImageResource(
                if (trend.isSaved) android.R.drawable.btn_star_big_on 
                else android.R.drawable.btn_star_big_off
            )
            
            btnSave.setOnClickListener { onSaveClick(trend) }

            ivImage.load(trend.imageUrl) {
                crossfade(true)
            }
        }
    }

    class TrendDiffCallback : DiffUtil.ItemCallback<Trend>() {
        override fun areItemsTheSame(oldItem: Trend, newItem: Trend) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Trend, newItem: Trend) = oldItem == newItem
    }
}
