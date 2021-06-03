package com.lunion.lunionapp.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.model.PredictionModel
import com.lunion.lunionapp.model.StatusProses
import com.lunion.lunionapp.model.UserModel

class DetectionViewModel(private val repository: LunionRepository) : ViewModel() {

    val dataUser: LiveData<UserModel>
    val saveDataTreatment: LiveData<StatusProses>
    val prediction: LiveData<PredictionModel>

    init {
        this.dataUser = repository.dataUser
        this.saveDataTreatment = repository.saveDataTreatment
        this.prediction = repository.predictionModel
    }

    fun getPrediction() = repository.getPrediction()

    fun checkEmailPatient(email: String) = repository.checkEmailPatient(email)

    fun getUserInfo() = repository.getUserInfo()

    fun saveDataTreatment(diagnose: String, confidence: String, note: String, user: UserModel, dataDoctor: UserModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            repository.saveDataTreatment(diagnose, confidence, note, user, dataDoctor)
        }
    }

}