package com.qerlly.touristapp.model

class CloseOpenModel(
    var id: String,
    var textOnOpen: String,
    var textOnClosed: String,
    var image: String,
    var pointStatus: Long
){
    companion object {
        fun new(tourPoint: TourPoint) = CloseOpenModel(tourPoint.id, tourPoint.title, tourPoint.description, tourPoint.image, tourPoint.status)
    }
}