package com.lunion.lunionapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.data.response.prediction.Prediction
import com.lunion.lunionapp.databinding.ActivityResultDetectionBinding
import com.lunion.lunionapp.model.PredictionModel
import com.lunion.lunionapp.model.UserModel
import com.lunion.lunionapp.utils.DataMapper
import com.lunion.lunionapp.viewmodel.DetectionViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory

class ResultDetectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultDetectionBinding
    lateinit var viewModel: DetectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[DetectionViewModel::class.java]
        viewModel.getUserInfo()
        viewModel.getPrediction()

        //get intent
        val user = intent.getParcelableExtra<UserModel>("DATA")
        binding.userName.text =
            user?.fullname?.split(" ")?.toTypedArray()?.get(0).toString() + " Lung"

        binding.btnSubmit.setOnClickListener {
            viewModel.dataUser.observe(this, { dataDoctor ->
                val note: String = binding.noteTreatment.text.toString()
                val diagnose: String = binding.tvDiagnoseType.text.toString()

                user?.let { it1 -> viewModel.saveDataTreatment(diagnose, note, it1, dataDoctor) }
            })
        }

        //GET prediction result intent
        viewModel.prediction.observe(this, {
            Log.d("resultdiagnose", it.prediction)

            binding.tvResultDiagnose.text = it.prediction
            binding.tvResultConfidence.text = it.confidence.toString()
        })

        //observer
        viewModel.saveDataTreatment.observe(this, {
            if (it.status == true) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                finish()
            }
        })

    }

    private fun bindToUi(predictionModel: PredictionModel) {
        binding.tvResultDiagnose.text = predictionModel.prediction
        binding.tvResultConfidence.text = predictionModel.confidence.toString()
    }
}