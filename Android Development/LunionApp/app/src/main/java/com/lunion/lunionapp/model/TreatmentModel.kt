package com.lunion.lunionapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TreatmentModel(
    val treatmentId: String? = null,
    val patientId: String? = null,
    val diagnose: String? = null,
    val note: String? = null,
    val doctorId: String? = null,
    val date: String? = null,
    val namePatient: String? = null,
    val nameDoctor: String? = null
):Parcelable
