package com.qerlly.touristapp.model.point

import com.qerlly.touristapp.model.openclose.CloseOpenModel

data class Point(
    override var id: String,
    override var textOnOpen: String,
    override var textOnClosed: String,
    private var lat: Double,
    private var long: Double,
) : CloseOpenModel(id, textOnOpen, textOnOpen)