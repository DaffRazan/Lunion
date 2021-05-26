package com.lunion.lunionapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lunion.lunionapp.databinding.FragmentDetectionBinding


class DetectionFragment : Fragment() {
    private lateinit var viewBinding: FragmentDetectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDetectionBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //select local file
        viewBinding.selectFile.setOnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, 1)
        }

        //detection n move resultDetectionActivity
        viewBinding.detectLung.setOnClickListener {
            val move = Intent(activity, ResultDetectionActivity::class.java)
            startActivity(move)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri? = data?.data
        viewBinding.text.text = uri.toString()
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