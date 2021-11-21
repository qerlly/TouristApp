package com.qerlly.touristapp.model.openclose

import androidx.room.PrimaryKey

open class CloseOpenModel(
    open var id: String,
    open var textOnOpen: String,
    open var textOnClosed: String,
)