package com.qerlly.touristapp.model

data class Tour (val id: Int, val name: String, val max_participants: Int, val price: Int, val start_date: String, val duration: String, val guide: Int, val participants: List<Int>)