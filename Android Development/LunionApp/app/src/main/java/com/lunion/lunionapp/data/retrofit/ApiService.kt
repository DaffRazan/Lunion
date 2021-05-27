package com.lunion.lunionapp.data.retrofit

import com.lunion.lunionapp.data.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    fun getAllNews(@Query("q") q: String?,
                    @Query("language") language: String?,
                    @Query("apiKey") apiKey: String?) : Call<NewsResponse>

}