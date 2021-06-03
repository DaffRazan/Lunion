package com.lunion.lunionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.databinding.ActivityResultDetectionBinding
import com.lunion.lunionapp.model.UserModel
import com.lunion.lunionapp.viewmodel.DetectionViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory

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
        binding.userName.text = user?.fullname?.split(" ")?.toTypedArray()?.get(0).toString() + " Lung"

        binding.btnSubmit.setOnClickListener {
            viewModel.dataUser.observe(this, { dataDoctor ->
                val note: String = binding.noteTreatment.text.toString()
                val diagnose: String = binding.tvResultDiagnose.text.toString()
                val confidence = binding.tvResultConfidence.text.toString()

                user?.let { it1 -> viewModel.saveDataTreatment(diagnose, confidence, note, it1, dataDoctor) }
            })
        }

        viewModel.prediction.observe(this, {
            Log.d("dataku", "dataa : "+it.prediction)

            binding.tvResultDiagnose.text = it.prediction

            val percent = it.confidence.toInt().toString() +"%"
            binding.tvResultConfidence.text = percent

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
        if (data){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}