package com.qerlly.touristapp.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.CloseOpenModel
import com.qerlly.touristapp.model.MemberPoint
import com.qerlly.touristapp.model.NewModel
import com.qerlly.touristapp.model.TourPoint
import com.qerlly.touristapp.repositories.TourRepository
import com.qerlly.touristapp.repositories.UserSettingsRepository
import com.qerlly.touristapp.ui.main.widgets.CloseOpenCardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(tourRepository: TourRepository, private val userSettingsRepository: UserSettingsRepository) : ViewModel(){
    /*val currentPointLatLong: MutableLiveData<Pair<Double, Double>> = MutableLiveData()

    fun sendCurrentLocation(latLong: Pair<Double, Double> ){
        Log.d("MainActivityViewModel", "got latLong");
    }*/

    private val openedCardIds: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())

    val pointsNameDesc: StateFlow<List<CloseOpenCardModel>?> =
        tourRepository.getTourPoints().combine(openedCardIds) { faqEntries, openedEntriesIds ->
            faqEntries.map { tourPoint ->
                val opened = tourPoint.id in openedEntriesIds
                CloseOpenCardModel(CloseOpenModel.new(tourPoint), opened)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun updateLocation(lat: String, long: String){
        userSettingsRepository.updateUserLocation(lat, long)
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
}