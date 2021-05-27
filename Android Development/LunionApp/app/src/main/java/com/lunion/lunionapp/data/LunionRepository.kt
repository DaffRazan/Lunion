package com.lunion.lunionapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lunion.lunionapp.data.response.Article
import com.lunion.lunionapp.data.response.NewsResponse
import com.lunion.lunionapp.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LunionRepository(private val apiRequest: ApiService) {

    companion object{
        @Volatile
        private var instance: LunionRepository? = null
        fun getInstance(apiRequest: ApiService): LunionRepository =
            instance ?: synchronized(this){
                instance?: LunionRepository(apiRequest)
            }
    }

    val news = MutableLiveData<List<Article>>()

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
                    Log.d("dataku", "retrofit error: $t")
                }

            })
    }

}