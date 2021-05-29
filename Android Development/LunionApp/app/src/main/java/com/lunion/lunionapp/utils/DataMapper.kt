package com.lunion.lunionapp.utils

import com.lunion.lunionapp.data.response.news.Article
import com.lunion.lunionapp.model.NewsModel

object DataMapper{
    fun mapArticleToNewsModel(data: Article): NewsModel{
        return NewsModel(
                author = data.author,
            content = data.content,
            description = data.description,
            publishedAt = data.publishedAt,
            source = data.source.name,
            title = data.title,
            url = data.url,
            urlToImage = data.urlToImage)
    }
}