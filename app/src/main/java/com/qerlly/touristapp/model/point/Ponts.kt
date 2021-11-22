package com.qerlly.touristapp.model.point

import com.qerlly.touristapp.model.openclose.CloseOpenModel

data class Point(
    override var id: String,
    override var textOnOpen: String,
    override var textOnClosed: String,
    var lat: Double,
    var long: Double,
) : CloseOpenModel(id, textOnOpen, textOnOpen)