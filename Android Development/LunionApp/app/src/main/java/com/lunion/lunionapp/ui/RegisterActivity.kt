package com.lunion.lunionapp.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.R
import com.lunion.lunionapp.databinding.ActivityRegisterBinding
import com.lunion.lunionapp.viewmodel.LoginRegisterViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: LoginRegisterViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[LoginRegisterViewModel::class.java]

        //go to login
        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        //show status register
        viewModel.registerSuccess.observe(this, {
            if (it.status == true) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                viewModel.typeUser.observe(this, { typeUser ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("DATA", typeUser)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                })
            } else if (it.status == false) {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun registerUser() {
        val fullName = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val type = checkRadio()

        when {
            TextUtils.isEmpty((fullName)) -> Toast.makeText(
                this,
                getString(R.string.text_fullname_required),
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty((email)) -> Toast.makeText(
                this,
                getString(R.string.text_email_required),
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty((password)) -> Toast.makeText(
                this,
                getString(R.string.text_password_required),
                Toast.LENGTH_LONG
            ).show()

            else -> {
                progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Sign Up")
                progressDialog.setMessage(getString(R.string.text_load_register))
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                viewModel.registerToApp(fullName, email, password, type)
            }
        }
    }

    private fun checkRadio(): String {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.patient -> {
                "patient"
            }
            R.id.doctor -> {
                "doctor"
            }
            else -> {
                "null"
            }
        }
    }

}