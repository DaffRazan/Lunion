package com.lunion.lunionapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lunion.lunionapp.databinding.ActivityDetailNewsBinding
import com.lunion.lunionapp.model.NewsModel

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val data = intent.getParcelableExtra<NewsModel>("data")
        if (data != null) {
            bindToUI(data)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bindToUI(news: NewsModel) {
        Glide.with(this)
                .load(news.urlToImage)
                .centerCrop()
                .into(binding.imgNewsArticle)
        binding.tvNewsTime.text = news.publishedAt.customDate()
        binding.tvNewsSource.text = news.source
        binding.tvNewsTitle.text = news.title
        binding.tvNewsContent.text = news.content
    }

    private fun String.customDate(): String{
        return this.split("T")[0]
    }

}