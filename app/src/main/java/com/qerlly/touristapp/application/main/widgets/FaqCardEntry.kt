package com.qerlly.touristapp.application.main.widgets

import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import com.qerlly.touristapp.databinding.FaqItemBinding
import com.qerlly.touristapp.model.faq.FaqEntry

data class FaqEntryCardModel(
    val faqEntry: FaqEntry,
    val expanded: Boolean = false,
) {
    companion object : DiffUtil.ItemCallback<FaqEntryCardModel>() {
        override fun areItemsTheSame(oldItem: FaqEntryCardModel, newItem: FaqEntryCardModel): Boolean {
            return oldItem.faqEntry.id == newItem.faqEntry.id
        }

        override fun areContentsTheSame(oldItem: FaqEntryCardModel, newItem: FaqEntryCardModel): Boolean {
            return oldItem == newItem
        }
    }
}

fun FaqItemBinding.bindTo(faqEntryCardModel: FaqEntryCardModel, onClick: (FaqEntryCardModel) -> Unit) {
    faqCard.setOnClickListener {
        onClick(faqEntryCardModel)
    }
    faqItemQuestion.text = faqEntryCardModel.faqEntry.question
    faqItemAnswer.text = HtmlCompat.fromHtml(faqEntryCardModel.faqEntry.answer, HtmlCompat.FROM_HTML_MODE_LEGACY)
    faqItemAnswer.visibility = if (faqEntryCardModel.expanded) View.VISIBLE else View.GONE
    faqItemArrow.animate().rotation(if (faqEntryCardModel.expanded) 180f else 0f)
}