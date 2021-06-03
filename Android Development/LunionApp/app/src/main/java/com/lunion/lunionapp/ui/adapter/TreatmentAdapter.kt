package com.lunion.lunionapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lunion.lunionapp.databinding.ItemsTreatmentBinding
import com.lunion.lunionapp.model.TreatmentModel

class TreatmentAdapter: RecyclerView.Adapter<TreatmentAdapter.ListViewHolder>() {

    private var listTreatment = ArrayList<TreatmentModel>()
    private var typeUser: String = ""

    fun setTreatment(data: List<TreatmentModel>){
        this.listTreatment = data as ArrayList<TreatmentModel>
        notifyDataSetChanged()
    }

    fun setTypeUser(type: String?){
        this.typeUser = type.toString()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TreatmentAdapter.ListViewHolder {
        val binding = ItemsTreatmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TreatmentAdapter.ListViewHolder, position: Int) {
        listTreatment[position].let { holder.bind(it) }
    }

    override fun getItemCount(): Int = listTreatment.size

    inner class ListViewHolder(private val binding: ItemsTreatmentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TreatmentModel){
            with(binding){
                if (typeUser == "doctor"){
                    doctorName.visibility = View.GONE
                    doctorNameText.visibility = View.GONE
                }else{
                    patientName.visibility = View.GONE
                    patientNameText.visibility = View.GONE
                }
                date.text = data.date
                doctorName.text = data.nameDoctor
                patientName.text = data.namePatient
                diagnosis.text = data.diagnose
                note.text = data.note
                confidence.text = data.confidence
            }
        }
    }

}