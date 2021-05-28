package com.lunion.lunionapp.data.retrofit

import com.lunion.lunionapp.data.response.air.AirQualityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceAirQuality {
    @GET("current/airquality")
    fun getAirQuality(@Query("lat") lat: Double?,
                   @Query("lon") lon: Double?,
                   @Query("key") key: String?) : Call<AirQualityResponse>
}