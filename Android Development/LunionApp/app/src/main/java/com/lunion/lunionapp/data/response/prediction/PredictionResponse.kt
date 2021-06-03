package com.lunion.lunionapp.data.response.prediction

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
  val predictions: List<Prediction>
)