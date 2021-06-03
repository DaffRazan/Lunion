package com.lunion.lunionapp.data.response.prediction

import com.google.gson.annotations.SerializedName

data class Prediction(
    @SerializedName("Confidence")
    val confidence: Double,

    @SerializedName("Prediction")
    val prediction: String
)
