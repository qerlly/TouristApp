package com.qerlly.touristapp.ui.main.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qerlly.touristapp.ui.layoutInflater
import com.qerlly.touristapp.ui.main.widgets.FaqEntryCardModel
import com.qerlly.touristapp.ui.main.widgets.bindTo
import com.qerlly.touristapp.databinding.FaqItemBinding

class FaqListAdapter(private val onClick: (FaqEntryCardModel) -> Unit) :
    ListAdapter<FaqEntryCardModel, FaqCardEntryViewHolder>(
        FaqEntryCardModel
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

class FaqCardEntryViewHolder(val faqItemBinding: FaqItemBinding) :
    RecyclerView.ViewHolder(faqItemBinding.root)