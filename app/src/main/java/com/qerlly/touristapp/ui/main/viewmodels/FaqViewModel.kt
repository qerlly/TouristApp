package com.qerlly.touristapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.ui.main.widgets.FaqEntryCardModel
import com.qerlly.touristapp.model.faq.FaqEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    faqEntryRepository: FaqEntryRepository,
) : ViewModel() {
    private val openedCardIds: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())

    val faqState: StateFlow<List<FaqEntryCardModel>?> =
        faqEntryRepository.getAll().combine(openedCardIds) { faqEntries, openedEntriesIds ->
            faqEntries.map { faqEntry ->
                val opened = faqEntry.id in openedEntriesIds
                FaqEntryCardModel(faqEntry, opened)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun onCardClicked(faqEntryCardModel: FaqEntryCardModel) {
        if (faqEntryCardModel.expanded) {
            closeCard(faqEntryCardModel)
        } else openCard(faqEntryCardModel)
    }

    private fun openCard(faqEntryCardModel: FaqEntryCardModel) {
        manipulateOpenedCardIds(faqEntryCardModel, MutableSet<String>::add)
    }

    private fun closeCard(faqEntryCardModel: FaqEntryCardModel) {
        manipulateOpenedCardIds(faqEntryCardModel, MutableSet<String>::remove)
    }

    private fun manipulateOpenedCardIds(
        faqEntryCardModel: FaqEntryCardModel,
        operation: MutableSet<String>.(String) -> Unit,
    ) {
        openedCardIds.value = openedCardIds.value.toMutableSet().apply {
            operation(faqEntryCardModel.faqEntry.id)
        }
    }
}