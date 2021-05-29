package com.lunion.lunionapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.data.response.air.Data

class AirQualityViewModel(private val repository: LunionRepository): ViewModel() {
    val airQuality: LiveData<Data>

    init {
        this.airQuality = repository.airQuality
    }

    fun getAirQuality(lat: Double, lon:Double){
        repository.getAirQuality(lat, lon)
    }

}