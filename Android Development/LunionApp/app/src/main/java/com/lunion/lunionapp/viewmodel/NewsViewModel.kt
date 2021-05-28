package com.lunion.lunionapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.data.response.news.Article

class NewsViewModel(private val repository: LunionRepository):  ViewModel() {

    val news: LiveData<List<Article>>

    init {
        this.news = repository.news
    }

    fun getAllNews() = repository.getAllNews()

}