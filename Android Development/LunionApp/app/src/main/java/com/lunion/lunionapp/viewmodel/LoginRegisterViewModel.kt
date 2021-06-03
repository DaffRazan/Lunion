package com.lunion.lunionapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lunion.lunionapp.data.LunionRepository
import com.lunion.lunionapp.model.StatusProses

class LoginRegisterViewModel(private val repository: LunionRepository): ViewModel() {

    val registerSuccess: LiveData<StatusProses>
    val typeUser: LiveData<String>

    init {
        this.registerSuccess = repository.registerSuccess
        this.typeUser = repository.typeUser
    }

    fun getuserInfo()  = repository.getUserInfo()

    fun loginToApp(email: String, password: String) = repository.loginToApp(email, password)

    fun registerToApp(fullname: String,
                        email: String,
                        password: String,
                        type: String) = repository.registerToApp(fullname,email, password, type)

}