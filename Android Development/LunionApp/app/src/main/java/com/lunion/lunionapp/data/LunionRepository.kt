package com.lunion.lunionapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lunion.lunionapp.data.response.air.AirQualityResponse
import com.lunion.lunionapp.data.response.air.Data
import com.lunion.lunionapp.data.response.news.Article
import com.lunion.lunionapp.data.response.news.NewsResponse
import com.lunion.lunionapp.data.retrofit.ApiService
import com.lunion.lunionapp.data.retrofit.ApiServiceAirQuality
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LunionRepository(private val apiRequest: ApiService, private val apiRequestAirQuality: ApiServiceAirQuality) {

    companion object{
        @Volatile
        private var instance: LunionRepository? = null
        fun getInstance(apiRequest: ApiService, apiRequestAirQuality: ApiServiceAirQuality): LunionRepository =
            instance ?: synchronized(this){
                instance?: LunionRepository(apiRequest, apiRequestAirQuality)
            }
    }

    val news = MutableLiveData<List<Article>>()
    val airQuality = MutableLiveData<Data>()

    fun getAllNews(){
        apiRequest.getAllNews("lung", "en", "883b0d47be8b4a439a13f94720316683")
            .enqueue(object : Callback<NewsResponse>{
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    news.postValue(response.body()?.articles)
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.d("Debug:", "retrofit error: $t")
                }

            })
    }

    fun getAirQuality(lat: Double, lon: Double){
        apiRequestAirQuality.getAirQuality(lat, lon, "eddd57c23f2e43918b81404fc95a9cec")
            .enqueue(object : Callback<AirQualityResponse>{
                override fun onResponse(
                    call: Call<AirQualityResponse>,
                    response: Response<AirQualityResponse>
                ) {
                    airQuality.postValue(response.body()?.data?.get(0))
                }

                override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                    Log.d("Debug:", "retrofit error: $t")
                }

            })
    }

}