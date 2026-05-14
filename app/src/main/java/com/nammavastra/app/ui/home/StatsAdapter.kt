package com.nammavastra.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nammavastra.app.R

class StatsAdapter : RecyclerView.Adapter<StatsAdapter.StatViewHolder>() {

    private var stats: List<StatMetric> = emptyList()

    fun submitList(newStats: List<StatMetric>) {
        stats = newStats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stat_card, parent, false)
        return StatViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        holder.bind(stats[position])
    }

    override fun getItemCount() = stats.size

    class StatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_stat_icon)
        private val tvValue: TextView = itemView.findViewById(R.id.tv_stat_value)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_stat_title)

        fun bind(metric: StatMetric) {
            ivIcon.setImageResource(metric.iconResId)
            tvValue.text = metric.value
            tvTitle.text = metric.title
        }
    }
}
