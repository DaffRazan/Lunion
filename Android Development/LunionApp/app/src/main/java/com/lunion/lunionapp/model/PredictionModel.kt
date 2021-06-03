package com.lunion.lunionapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictionModel(
    val confidence: Double,
    val prediction: String
) : Parcelable
