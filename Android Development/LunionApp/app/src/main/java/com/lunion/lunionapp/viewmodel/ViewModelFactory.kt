package com.lunion.lunionapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.di.Injection

class ViewModelFactory private constructor(private val repository: LunionRepository) : ViewModelProvider.NewInstanceFactory() {
    companion object{
        @Volatile
        private var instance: ViewModelFactory?= null

        fun getInstance(): ViewModelFactory =
            instance?: synchronized(this){
                instance?: ViewModelFactory(Injection.provideRepository())
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(NewsViewModel::class.java)->{
                NewsViewModel(repository) as T
            }
            else -> throw Throwable("UnKnow ViewModel class: " + modelClass.name)
        }
    }
}