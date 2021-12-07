package com.qerlly.touristapp.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qerlly.touristapp.model.point.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel(){
    val currentPointLatLong: MutableLiveData<Pair<Double, Double>> = MutableLiveData()

    fun sendCurrentLocation(latLong: Pair<Double, Double> ){
        Log.d("MainActivityViewModel", "got latLong");
    }
}