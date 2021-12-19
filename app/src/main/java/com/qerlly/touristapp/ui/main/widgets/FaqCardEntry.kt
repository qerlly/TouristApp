package com.qerlly.touristapp.ui.main.widgets

import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import com.qerlly.touristapp.databinding.FaqItemBinding
import com.qerlly.touristapp.model.CloseOpenModel

data class CloseOpenCardModel(
    val closeOpenModel: CloseOpenModel,
    val expanded: Boolean = false,
) {
    companion object : DiffUtil.ItemCallback<CloseOpenCardModel>() {
        override fun areItemsTheSame(oldItem: CloseOpenCardModel, newItem: CloseOpenCardModel): Boolean {
            return oldItem.closeOpenModel.id == newItem.closeOpenModel.id
        }

        override fun areContentsTheSame(oldItem: CloseOpenCardModel, newItem: CloseOpenCardModel): Boolean {
            return oldItem == newItem
        }
    }
}

fun FaqItemBinding.bindTo(closeOpenCardModel: CloseOpenCardModel, onClick: (CloseOpenCardModel) -> Unit) {
    faqCard.setOnClickListener {
        onClick(closeOpenCardModel)
    }
    faqItemQuestion.text = closeOpenCardModel.closeOpenModel.textOnOpen
    faqItemAnswer.text = HtmlCompat.fromHtml(closeOpenCardModel.closeOpenModel.textOnClosed, HtmlCompat.FROM_HTML_MODE_LEGACY)
    faqItemAnswer.visibility = if (closeOpenCardModel.expanded) View.VISIBLE else View.GONE
    faqItemArrow.animate().rotation(if (closeOpenCardModel.expanded) 180f else 0f)
}