package com.nammavastra.app.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.card.MaterialCardView
import com.nammavastra.app.R
import com.nammavastra.app.data.model.Trend

class GalleryAdapter(
    private val onClick: (Trend, Int) -> Unit,
    private val onLongClick: (Trend) -> Unit
) : ListAdapter<Trend, GalleryAdapter.GalleryViewHolder>(GalleryDiffCallback()) {

    private var selectedIds: Set<String> = emptySet()

    fun updateSelection(ids: Set<String>) {
        selectedIds = ids
        notifyDataSetChanged() // Ideally use payload for better performance
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_card, parent, false)
        return GalleryViewHolder(view, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position), selectedIds.contains(getItem(position).id))
    }

    class GalleryViewHolder(
        itemView: View,
        private val onClick: (Trend, Int) -> Unit,
        private val onLongClick: (Trend) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val ivSaree: ImageView = itemView.findViewById(R.id.iv_saree)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val viewSelection: View = itemView.findViewById(R.id.view_selection_overlay)
        private val ivCheck: ImageView = itemView.findViewById(R.id.iv_check)
        private val cardRoot: MaterialCardView = itemView.findViewById(R.id.card_root)

        fun bind(trend: Trend, isSelected: Boolean) {
            tvTitle.text = trend.title
            
            // For shared element transition
            ivSaree.transitionName = "saree_image_${trend.id}"

            ivSaree.load(trend.imageUrl) {
                crossfade(true)
            }

            if (isSelected) {
                viewSelection.visibility = View.VISIBLE
                ivCheck.visibility = View.VISIBLE
                cardRoot.strokeWidth = 4
                cardRoot.strokeColor = itemView.context.getColor(android.R.color.holo_blue_light)
            } else {
                viewSelection.visibility = View.GONE
                ivCheck.visibility = View.GONE
                cardRoot.strokeWidth = 0
            }

            cardRoot.setOnClickListener {
                onClick(trend, adapterPosition)
            }

            cardRoot.setOnLongClickListener {
                onLongClick(trend)
                true
            }
        }
    }

    class GalleryDiffCallback : DiffUtil.ItemCallback<Trend>() {
        override fun areItemsTheSame(oldItem: Trend, newItem: Trend) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Trend, newItem: Trend) = oldItem == newItem
    }
}
