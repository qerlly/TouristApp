package com.qerlly.touristapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.point.Point
import com.qerlly.touristapp.model.point.PointsRepository
import com.qerlly.touristapp.ui.main.widgets.CloseOpenCardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel

class PointsViewModel
    @Inject constructor(pointsRepository: PointsRepository) : ViewModel() {

    private val openedCardIds: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())

    val pointsNameDesc: StateFlow<List<CloseOpenCardModel>?> =
        pointsRepository.getAll().combine(openedCardIds) { faqEntries, openedEntriesIds ->
            faqEntries.map { faqEntry ->
                val opened = faqEntry.id in openedEntriesIds
                CloseOpenCardModel(faqEntry, opened)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val pointsCoordinates: StateFlow<List<Point>?> =
        pointsRepository.getAll()

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