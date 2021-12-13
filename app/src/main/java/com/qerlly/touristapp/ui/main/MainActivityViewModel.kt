package com.qerlly.touristapp.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel(){
    val currentPointLatLong: MutableLiveData<Pair<Double, Double>> = MutableLiveData()

    fun sendCurrentLocation(latLong: Pair<Double, Double> ){
        Log.d("MainActivityViewModel", "got latLong");
    }
}