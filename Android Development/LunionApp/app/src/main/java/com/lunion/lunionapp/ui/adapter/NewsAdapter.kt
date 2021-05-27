package com.lunion.lunionapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lunion.lunionapp.databinding.ItemsNewsBinding
import com.lunion.lunionapp.model.NewsItem

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var listNews: List<NewsItem> = arrayListOf()
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setNews(news: List<NewsItem>) {
        this.listNews = news
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(private val binding: ItemsNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsItem) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        val itemsNewsBinding =
            ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(itemsNewsBinding)
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        val news = listNews[position]
    }

    override fun getItemCount() = listNews.size
}