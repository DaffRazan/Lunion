package com.lunion.lunionapp.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.lunion.lunionapp.databinding.ActivityLoginBinding
import com.lunion.lunionapp.viewmodel.LoginRegisterViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //loading
        checkIsLoading(true)

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[LoginRegisterViewModel::class.java]

        if (FirebaseAuth.getInstance().currentUser != null){
            viewModel.getuserInfo()
            viewModel.typeUser.observe(this,{
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("DATA", it)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            })
        }else{
            checkIsLoading(false)
        }

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
                progressDialog.dismiss()
                viewModel.typeUser.observe(this, {typeUser ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("DATA", typeUser)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                })
            }else if(it.status == false){
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
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
                progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                viewModel.loginToApp(email, password)

            }
        }
    }

    private fun checkIsLoading(data: Boolean) {
        if (data){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}