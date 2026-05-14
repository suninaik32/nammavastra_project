package com.nammavastra.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nammavastra.app.R
import com.nammavastra.app.data.model.Saree

class RecentUploadsAdapter : RecyclerView.Adapter<RecentUploadsAdapter.UploadViewHolder>() {

    private var uploads: List<Saree> = emptyList()

    fun submitList(newUploads: List<Saree>) {
        uploads = newUploads
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_upload, parent, false)
        return UploadViewHolder(view)
    }

    override fun onBindViewHolder(holder: UploadViewHolder, position: Int) {
        holder.bind(uploads[position])
    }

    override fun getItemCount() = uploads.size

    class UploadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_saree_image)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_saree_title)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_saree_price)

        fun bind(saree: Saree) {
            tvTitle.text = saree.title
            tvPrice.text = "₹${saree.price.toInt()}"
            
            ivImage.load(saree.imageUrl) {
                crossfade(true)
            }
        }
    }
}
