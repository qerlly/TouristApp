package com.qerlly.touristapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.point.Point
import com.qerlly.touristapp.model.point.PointsRepository
import com.qerlly.touristapp.model.user.UserLoc
import com.qerlly.touristapp.ui.main.widgets.CloseOpenCardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UserLocationsViewModel @Inject constructor(pointsRepository: PointsRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    val userLocations: StateFlow<List<UserLoc>?> =
        pointsRepository.getUserLocations()
}