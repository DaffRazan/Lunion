package com.lunion.lunionapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lunion.lunionapp.data.response.news.Article
import com.lunion.lunionapp.databinding.ItemsNewsBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {

    private var listNews = ArrayList<Article>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Article)
    }

    fun setNews(news: List<Article>){
        this.listNews = news as ArrayList<Article>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        listNews[position].let { holder.bind(it) }
    }

    override fun getItemCount(): Int = listNews.size

    inner class ListViewHolder(private val binding: ItemsNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: Article) {
            with(binding){
                Glide.with(itemView.context)
                    .load(news.urlToImage)
                    .centerCrop()
                    .into(imgPoster)
                newsTitle.text = news.title
                newsDate.text = news.publishedAt.customDate()
                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(news)}
            }
        }
    }

    private fun String.customDate(): String{
        return this.split("T")[0]
    }
}