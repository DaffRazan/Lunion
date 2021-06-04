package com.lunion.lunionapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.lunion.lunionapp.R
import com.lunion.lunionapp.databinding.ActivityResultDetectionBinding
import com.lunion.lunionapp.model.UserModel
import com.lunion.lunionapp.viewmodel.DetectionViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory
import java.math.RoundingMode
import java.text.DecimalFormat

class ResultDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDetectionBinding
    lateinit var viewModel: DetectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //loading
        checkIsLoading(true)

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[DetectionViewModel::class.java]
        viewModel.getUserInfo()
        viewModel.getPrediction()

        //get intent
        val user = intent.getParcelableExtra<UserModel>("DATA")
        binding.userName.text =
            user?.fullname?.split(" ")?.toTypedArray()?.get(0).toString() + "'s Lung"

        binding.btnSubmit.setOnClickListener {
            if (binding.noteTreatment.text.isNullOrEmpty()) {
                val snackBar = Snackbar.make(
                    it, "Please give some notes!",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.setActionTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.primary_color
                    )
                )
                val textView =
                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                snackBar.show()
            } else {
                viewModel.dataUser.observe(this, { dataDoctor ->
                    val note: String = binding.noteTreatment.text.toString()
                    val diagnose: String = binding.tvResultDiagnose.text.toString()
                    val confidence = binding.tvResultConfidenceText.text.toString()
                    user?.let { it1 ->
                        viewModel.saveDataTreatment(
                            diagnose,
                            confidence,
                            note,
                            it1,
                            dataDoctor
                        )
                    }
                })
            }
        }

        viewModel.prediction.observe(this, {
            when (it.prediction) {
                "Non-Chronic Disease" -> {
                    binding.tvResultPossibleTypeDisease.text =
                        StringBuilder("URTI, LRTI, Pneumonia, Bronchiolitis").toString()
                }
                "Chronic Disease" -> {
                    binding.tvResultPossibleTypeDisease.text =
                        StringBuilder("COPD, Bronchiectasis, Asthma").toString()
                }
                "Healthy" -> {
                    binding.tvResultPossibleTypeDisease.text =
                        StringBuilder("Keep your lung healthy").toString()
                }
                else -> {
                    binding.tvResultPossibleTypeDisease.text =
                        StringBuilder("null").toString()
                }
            }

            val df = DecimalFormat("#.##")

            binding.tvResultDiagnose.text = it.prediction
            binding.tvResultConfidenceText.text =
                StringBuilder(df.format(it.confidence)).append("%")

            checkIsLoading(false)
        })

        //observer
        viewModel.saveDataTreatment.observe(this, {
            if (it.status == true) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                finish()
            }
        })

    }

    private fun checkIsLoading(data: Boolean) {
        if (data) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
       showAlertDialog()
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Discard changes?")
        alertDialogBuilder
            .setMessage("Prediction result is gone when you go back")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                    finish()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}