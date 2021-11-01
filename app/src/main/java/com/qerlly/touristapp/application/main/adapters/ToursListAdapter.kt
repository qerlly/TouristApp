package com.qerlly.touristapp.application.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qerlly.touristapp.R
import com.qerlly.touristapp.model.Tour
import kotlinx.android.synthetic.main.tour_list_view.view.*

class ToursListAdapter : RecyclerView.Adapter<ToursListAdapter.ToursViewHolder>() {

    inner class ToursViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object : DiffUtil.ItemCallback<Tour>() {
        override fun areItemsTheSame(oldItem: Tour, newItem: Tour): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tour, newItem: Tour): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToursViewHolder {
        return ToursViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tour_list_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ToursViewHolder, position: Int) {
        val tour = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .into(tour_image)

            tour_name.text = tour.name
            tour_desc.text = tour.roadmap
            tour_time.text = tour.date
            tour_card.setOnClickListener {
                onItemClickListener?.let {
                    it(tour)
                }
            }
        }
    }

    private var onItemClickListener: ((Tour) -> Unit)? = null

    fun setOnClickListener(listener: (Tour) -> Unit) {
        onItemClickListener = listener
    }
}