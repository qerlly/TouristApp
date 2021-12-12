package com.qerlly.touristapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.TourModel
import com.qerlly.touristapp.repositories.ToursRepository
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ToursViewModel @Inject constructor(
    toursRepository: ToursRepository,
    private val settingsService: SettingsService
) : ViewModel() {

    fun joinToTour(id: String) = runBlocking { settingsService.setTour(id) }

    val toursState: StateFlow<List<TourModel>?> =
        toursRepository.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}