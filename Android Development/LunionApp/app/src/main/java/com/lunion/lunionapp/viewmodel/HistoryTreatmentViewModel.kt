package com.lunion.lunionapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.model.TreatmentModel

class HistoryTreatmentViewModel(private val repository: LunionRepository): ViewModel() {

    val dataTreatment: LiveData<List<TreatmentModel>>

    init {
        this.dataTreatment = repository.dataTreatment
    }

    fun getAllTreatment(userId: String, type: String){
        repository.getAllTreatment(userId, type)
    }

}