package com.qerlly.touristapp.model.point

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PointsRepository (
    private val points: MutableStateFlow<List<Point>> = MutableStateFlow(mutableListOf())
) {
    init {
        points.value = listOf(
            Point(
                "1",
                "Kraków Główny",
                "Najstarszy dworzec pkp Krakowa",
                50.056999772,
                19.935662924,
            ),
            Point(
                "2",
                "Wrocław główny",
                "Najstarszy dworzec pkp Wrocławia",
                51.0980555556,
                17.0375,
            )
        )
    }
    fun getAll(): MutableStateFlow<List<Point>> = points
}