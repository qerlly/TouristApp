package com.qerlly.touristapp.application

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.getSystemService

val Context.layoutInflater: LayoutInflater
    get() = getSystemService()!!