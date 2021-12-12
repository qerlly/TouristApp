package com.qerlly.touristapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.TourModel
import com.qerlly.touristapp.repositories.ToursRepository
import com.qerlly.touristapp.repositories.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ToursViewModel @Inject constructor(
    toursRepository: ToursRepository,
    private val userRepository: UserSettingsRepository
) : ViewModel() {

    fun joinToTour(id: String) = userRepository.saveTour(id)

    val toursState: StateFlow<List<TourModel>?> =
        toursRepository.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}