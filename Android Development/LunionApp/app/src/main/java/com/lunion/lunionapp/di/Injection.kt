package com.lunion.lunionapp.di

import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(): LunionRepository{
        return LunionRepository.getInstance(ApiConfig.make(), ApiConfig.makeAirQuality(), ApiConfig.makePredict())
    }
}