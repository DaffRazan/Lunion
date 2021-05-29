package com.lunion.lunionapp.data.response.air

data class Data(
    val aqi: Int,
    val co: Double,
    val mold_level: Int,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm25: Double,
    val pollen_level_grass: Int,
    val pollen_level_tree: Int,
    val pollen_level_weed: Int,
    val predominant_pollen_type: String,
    val so2: Double
)