package com.qerlly.touristapp.application.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.application.main.widgets.CloseOpenCardModel
import com.qerlly.touristapp.model.faq.FaqEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    faqEntryRepository: FaqEntryRepository,
) : ViewModel() {
    private val openedCardIds: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())

    val faqState: StateFlow<List<CloseOpenCardModel>?> =
        faqEntryRepository.getAll().combine(openedCardIds) { faqEntries, openedEntriesIds ->
            faqEntries.map { faqEntry ->
                val opened = faqEntry.id in openedEntriesIds
                CloseOpenCardModel(faqEntry, opened)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun onCardClicked(closeOpenCardModel: CloseOpenCardModel) {
        if (closeOpenCardModel.expanded) {
            closeCard(closeOpenCardModel)
        } else openCard(closeOpenCardModel)
    }

    private fun openCard(closeOpenCardModel: CloseOpenCardModel) {
        manipulateOpenedCardIds(closeOpenCardModel, MutableSet<String>::add)
    }

    private fun closeCard(closeOpenCardModel: CloseOpenCardModel) {
        manipulateOpenedCardIds(closeOpenCardModel, MutableSet<String>::remove)
    }

    private fun manipulateOpenedCardIds(
        closeOpenCardModel: CloseOpenCardModel,
        operation: MutableSet<String>.(String) -> Unit,
    ) {
        openedCardIds.value = openedCardIds.value.toMutableSet().apply {
            operation(closeOpenCardModel.closeOpenModel.id)
        }
    }
}