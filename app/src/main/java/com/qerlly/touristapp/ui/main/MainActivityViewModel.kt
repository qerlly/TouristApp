package com.qerlly.touristapp.ui.main

import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.CloseOpenModel
import com.qerlly.touristapp.model.MemberPoint
import com.qerlly.touristapp.model.TourPoint
import com.qerlly.touristapp.receivers.LocationState
import com.qerlly.touristapp.receivers.LocationStateReceiver
import com.qerlly.touristapp.repositories.TourRepository
import com.qerlly.touristapp.repositories.UserSettingsRepository
import com.qerlly.touristapp.services.SettingsService
import com.qerlly.touristapp.ui.main.widgets.CloseOpenCardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    tourRepository: TourRepository,
    private val userSettingsRepository: UserSettingsRepository,
    private val settingsService: SettingsService,
    private val locationReceiver: LocationStateReceiver,
) : ViewModel() {

    val localizationState: Flow<Boolean> =
        locationReceiver.state
            .map { it == LocationState.ENABLED }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    private val openedCardIds: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())

    val pointsNameDesc: StateFlow<List<CloseOpenCardModel>?> =
        tourRepository.getTourPoints().combine(openedCardIds) { faqEntries, openedEntriesIds ->
            faqEntries.map { tourPoint ->
                val opened = tourPoint.id in openedEntriesIds
                CloseOpenCardModel(CloseOpenModel.new(tourPoint), opened)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun updateLocation(lat: String, long: String) {
        viewModelScope.launch {
            val localization = settingsService.getUserLocalization().first()
            if (localization) userSettingsRepository.updateUserLocation(lat, long)
            else userSettingsRepository.updateUserLocation("", "")
        }
    }

    val tourPoints: StateFlow<List<TourPoint>?> =
        tourRepository.getTourPoints().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val membersPoints: StateFlow<List<MemberPoint>?> =
        tourRepository.getMembersPoints().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

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

    override fun onCleared() {
        super.onCleared()
        locationReceiver.unregisterReceiver()
    }
}