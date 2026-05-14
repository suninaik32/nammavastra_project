package com.nammavastra.app.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.chrisbanes.photoview.PhotoView
import com.nammavastra.app.R
import com.nammavastra.app.data.model.Trend

class FullscreenPagerAdapter(
    private val items: List<Trend>
) : RecyclerView.Adapter<FullscreenPagerAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saree_fullscreen, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoView: PhotoView = itemView.findViewById(R.id.photo_view)

        fun bind(trend: Trend) {
            photoView.load(trend.imageUrl) {
                crossfade(true)
            }
        }
    }
}
