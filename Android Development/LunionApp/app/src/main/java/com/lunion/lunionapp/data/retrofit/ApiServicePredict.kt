package com.lunion.lunionapp.data.retrofit

import com.lunion.lunionapp.data.response.prediction.PredictionResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiServicePredict {
    @GET("get")
    fun getPredictionResult() : Call<PredictionResponse>
}