package com.lunion.lunionapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lunion.lunionapp.databinding.ItemsTreatmentBinding
import com.lunion.lunionapp.model.TreatmentModel

class TreatmentAdapter: RecyclerView.Adapter<TreatmentAdapter.ListViewHolder>() {

    private var listTreatment = ArrayList<TreatmentModel>()

    fun setTreatment(data: List<TreatmentModel>){
        this.listTreatment = data as ArrayList<TreatmentModel>
        notifyDataSetChanged()
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
                date.text = data.date
                doctorName.text = data.nameDoctor
                patientName.text = data.namePatient
                diagnosis.text = data.diagnose
                note.text = data.note
            }
        }
    }

}