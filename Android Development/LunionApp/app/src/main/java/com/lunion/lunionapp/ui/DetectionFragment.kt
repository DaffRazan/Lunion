package com.lunion.lunionapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lunion.lunionapp.R
import com.lunion.lunionapp.databinding.FragmentDetectionBinding
import com.lunion.lunionapp.model.UserModel
import com.lunion.lunionapp.viewmodel.DetectionViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory


class DetectionFragment : Fragment() {
    private lateinit var viewBinding: FragmentDetectionBinding
    private lateinit var viewModel: DetectionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDetectionBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[DetectionViewModel::class.java]

        //select local file
        viewBinding.btnSelectFile.setOnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, 1)
        }

        //detection n move resultDetectionActivity
        viewBinding.btnDetectLung.setOnClickListener {
            viewModel.checkEmailPatient(viewBinding.emailPatient.text.toString())
        }

        viewModel.dataUser.observe(viewLifecycleOwner, {
            if (it != null){
                    Log.d("dataku", "data: ${it.email}")
                    moveResultDetection(it)
            }else{
                Toast.makeText(requireContext(), "Email doesn't exists...", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun moveResultDetection(data: UserModel){
        val move = Intent(activity, ResultDetectionActivity::class.java)
        move.putExtra("DATA", data)
        startActivity(move)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri? = data?.data
        viewBinding.tvFileName.text = uri.toString()
    }

    companion object {
        fun newInstance(): DetectionFragment {
            val fragment = DetectionFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


}