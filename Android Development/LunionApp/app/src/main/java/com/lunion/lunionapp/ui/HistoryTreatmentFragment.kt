package com.lunion.lunionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.lunion.lunionapp.R
import com.lunion.lunionapp.databinding.FragmentHistoryTreatmentBinding
import com.lunion.lunionapp.ui.adapter.TreatmentAdapter
import com.lunion.lunionapp.viewmodel.HistoryTreatmentViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory
import kotlin.math.log

class HistoryTreatmentFragment : Fragment() {

    private lateinit var viewBinding: FragmentHistoryTreatmentBinding
    private lateinit var viewModel: HistoryTreatmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHistoryTreatmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //checkLoading
        checkIsLoading(true)

        //type user
        val typeUser = arguments?.getString("DATA")
        Log.d("dataku", "type: $typeUser")

        // To be added with active Username
        val nameFirebase = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val userActiveName = resources.getString(R.string.text_greetings, "User")
        viewBinding.tvGreetings.text = userActiveName

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[HistoryTreatmentViewModel::class.java]
        viewModel.getAllTreatment(
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            typeUser.toString()
        )

        //recyclerView
        viewBinding.rvTreatment.setHasFixedSize(true)
        showRecyclerView()
    }

    private fun showRecyclerView() {
        checkIsLoading(true)
        viewBinding.rvTreatment.layoutManager = LinearLayoutManager(context)
        val adapter = TreatmentAdapter()
        viewModel.dataTreatment.observe(viewLifecycleOwner, {
            if (it == null) {
                viewBinding.lottieNoData.visibility = View.VISIBLE
                viewBinding.textNoData.visibility = View.VISIBLE
            } else {
                adapter.setTreatment(it)
            }
            checkIsLoading(false)
        })
        viewBinding.rvTreatment.adapter = adapter
    }

    private fun checkIsLoading(data: Boolean) {
        if (data) {
            viewBinding.progressBar.visibility = View.VISIBLE
        } else {
            viewBinding.progressBar.visibility = View.INVISIBLE
        }
    }

    companion object {
        fun newInstance(): HistoryTreatmentFragment {
            val fragment = HistoryTreatmentFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}