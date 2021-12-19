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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(tourRepository: TourRepository) : ViewModel(){
    /*val currentPointLatLong: MutableLiveData<Pair<Double, Double>> = MutableLiveData()

    fun sendCurrentLocation(latLong: Pair<Double, Double> ){
        Log.d("MainActivityViewModel", "got latLong");
    }*/

    val tourPoints: StateFlow<List<TourPoint>?> =
        tourRepository.getTourPoints().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val membersPoints: StateFlow<List<MemberPoint>?> =
        tourRepository.getMembersPoints().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}