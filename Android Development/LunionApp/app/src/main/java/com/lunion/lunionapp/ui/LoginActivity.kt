package com.lunion.lunionapp.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.databinding.ActivityLoginBinding
import com.lunion.lunionapp.viewmodel.LoginRegisterViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progrssDialog: ProgressDialog
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[LoginRegisterViewModel::class.java]

        //go to register
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        //show status login
        viewModel.registerSuccess.observe(this, {
            if (it.status == true){
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                progrssDialog.dismiss()
                viewModel.typeUser.observe(this, {typeUser ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("DATA", typeUser)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                })
            }else if(it.status == false){
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                progrssDialog.dismiss()
            }
        })

    }

    private fun loginUser() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        when{
            TextUtils.isEmpty((email)) -> Toast.makeText(
                this,
                "email is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty((password)) -> Toast.makeText(
                this,
                "password is required",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                progrssDialog = ProgressDialog(this)
                progrssDialog.setTitle("Login")
                progrssDialog.setMessage("Please wait, this may take a while...")
                progrssDialog.setCanceledOnTouchOutside(false)
                progrssDialog.show()

                viewModel.loginToApp(email, password)

            }
        }
    }

}