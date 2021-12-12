package com.qerlly.touristapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.NewModel
import com.qerlly.touristapp.model.TourModel
import com.qerlly.touristapp.model.TourPoint
import com.qerlly.touristapp.repositories.TourRepository
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TourViewModel @Inject constructor(
    tourRepository: TourRepository,
    private val settingsService: SettingsService
) : ViewModel() {

    fun leaveTour() = runBlocking { settingsService.setTour("") }

    val tourState: StateFlow<TourModel?> =
        tourRepository.getByTourID()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val tourNews: StateFlow<List<NewModel>?> =
        tourRepository.getTourAnnouncements().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val points: StateFlow<List<TourPoint>?> =
        tourRepository.getTourPoints().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}