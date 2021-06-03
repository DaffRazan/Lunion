package com.lunion.lunionapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.model.TreatmentModel
import com.lunion.lunionapp.model.UserModel

class HistoryTreatmentViewModel(private val repository: LunionRepository): ViewModel() {

    val dataTreatment: LiveData<List<TreatmentModel>>
    val dataUser: LiveData<UserModel>

    init {
        this.dataTreatment = repository.dataTreatment
        this.dataUser = repository.dataUser
    }

    fun getAllTreatment(userId: String, type: String){
        repository.getAllTreatment(userId, type)
    }

    fun getUserInfo() = repository.getUserInfo()

}