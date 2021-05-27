package com.lunion.lunionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.databinding.FragmentNewsBinding
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
        viewModel.news.observe(viewLifecycleOwner,{
            Log.d("dataku", "data = ${it[0]}")
        })

    }

}