package com.qerlly.touristapp.ui.main.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qerlly.touristapp.databinding.FaqItemBinding
import com.qerlly.touristapp.ui.layoutInflater
import com.qerlly.touristapp.ui.main.widgets.CloseOpenCardModel
import com.qerlly.touristapp.ui.main.widgets.bindTo

class FaqListAdapter(private val onClick: (CloseOpenCardModel) -> Unit) : ListAdapter<CloseOpenCardModel, FaqCardEntryViewHolder>(
    CloseOpenCardModel
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqCardEntryViewHolder {
        return FaqCardEntryViewHolder(
            FaqItemBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FaqCardEntryViewHolder, position: Int) {
        holder.faqItemBinding.bindTo(getItem(position), onClick)
    }
}

class FaqCardEntryViewHolder(val faqItemBinding: FaqItemBinding) : RecyclerView.ViewHolder(faqItemBinding.root)