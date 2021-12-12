package com.qerlly.touristapp.model.point

import com.qerlly.touristapp.model.user.UserLoc
import kotlinx.coroutines.flow.MutableStateFlow

class PointsRepository(
    private val pointsOfAttractions: MutableStateFlow<List<Point>> = MutableStateFlow(mutableListOf()),
    private val userLocations: MutableStateFlow<List<UserLoc>> = MutableStateFlow(mutableListOf())
) {
    init {
        pointsOfAttractions.value = listOf(
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

        userLocations.value = listOf(
            UserLoc(
                "1",
                50.056999772,
                19.935662924,
                false,
                true
            ),
            UserLoc(
                "2",
                51.0980555556,
                17.0375,
                false,
                false
            ),
            UserLoc(
                "3",
                50.156999772,
                19.935662924,
                true,
                false
            ),
            UserLoc(
                "4",
                50.256999772,
                19.975662924,
                false,
                false
            )
        )
    }

    fun getAllPointsOfLocations(): MutableStateFlow<List<Point>> = pointsOfAttractions
    fun getUserLocations(): MutableStateFlow<List<UserLoc>> = userLocations
}