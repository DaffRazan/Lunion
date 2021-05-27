package com.lunion.lunionapp.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private val httpClient = OkHttpClient.Builder().build()

    fun make(): ApiService {
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
        return retrofit.create(ApiService::class.java)
    }


}