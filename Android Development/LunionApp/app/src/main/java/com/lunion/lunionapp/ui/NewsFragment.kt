package com.lunion.lunionapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunion.lunionapp.data.response.Article
import com.lunion.lunionapp.data.response.Source
import com.lunion.lunionapp.databinding.FragmentNewsBinding
import com.lunion.lunionapp.model.NewsModel
import com.lunion.lunionapp.ui.adapter.NewsAdapter
import com.lunion.lunionapp.utils.DataMapper
import com.lunion.lunionapp.viewmodel.NewsViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
        viewModel.getAllNews()

        //recyclerView
        binding.rvNews.setHasFixedSize(true)
        showRecyclerView()

    }

    private fun showRecyclerView() {
        checkIsLoading(true)
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        val adapter = NewsAdapter()
        viewModel.news.observe(viewLifecycleOwner,{
            adapter.setNews(it)
            checkIsLoading(false)
        })
        binding.rvNews.adapter = adapter

//        when item selected
        adapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Article) {
                selectedNews(data)
            }
        })
    }

    private fun selectedNews(data: Article){
        val moveDetail = Intent(context, DetailNewsActivity::class.java)
        moveDetail.putExtra("data", DataMapper.mapArticleToNewsModel(data))
        startActivity(moveDetail)
    }

    private fun checkIsLoading(data: Boolean) {
        if (data){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}