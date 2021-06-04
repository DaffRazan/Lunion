package com.lunion.lunionapp.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lunion.lunionapp.BuildConfig.API_KEY_AQI
import com.lunion.lunionapp.BuildConfig.API_KEY_NEWS
import com.lunion.lunionapp.data.response.air.AirQualityResponse
import com.lunion.lunionapp.data.response.air.Data
import com.lunion.lunionapp.data.response.news.Article
import com.lunion.lunionapp.data.response.news.NewsResponse
import com.lunion.lunionapp.data.response.prediction.PredictResponse
import com.lunion.lunionapp.data.retrofit.ApiService
import com.lunion.lunionapp.data.retrofit.ApiServiceAirQuality
import com.lunion.lunionapp.data.retrofit.ApiServicePredict
import com.lunion.lunionapp.model.PredictionModel
import com.lunion.lunionapp.model.StatusProses
import com.lunion.lunionapp.model.TreatmentModel
import com.lunion.lunionapp.model.UserModel
import com.lunion.lunionapp.utils.Constants.BUCKET_LINK
import com.lunion.lunionapp.utils.DataMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LunionRepository(
    private val apiRequest: ApiService,
    private val apiRequestAirQuality: ApiServiceAirQuality,
    private val apiRequestPredict: ApiServicePredict
) {

    companion object {
        @Volatile
        private var instance: LunionRepository? = null
        fun getInstance(
            apiRequest: ApiService,
            apiRequestAirQuality: ApiServiceAirQuality,
            apiRequestPredict: ApiServicePredict
        ): LunionRepository =
            instance ?: synchronized(this) {
                instance ?: LunionRepository(apiRequest, apiRequestAirQuality, apiRequestPredict)
            }
    }

    val news = MutableLiveData<List<Article>>()
    val predictionModel = MutableLiveData<PredictionModel>()
    val airQuality = MutableLiveData<Data>()
    val registerSuccess = MutableLiveData<StatusProses>()
    val typeUser = MutableLiveData<String>()
    val dataUser = MutableLiveData<UserModel>()
    val saveDataTreatment = MutableLiveData<StatusProses>()
    val dataTreatment = MutableLiveData<List<TreatmentModel>>()

    fun getPrediction() {
        apiRequestPredict.getPredictionResult()
            .enqueue(object : Callback<PredictResponse> {
                override fun onResponse(
                    call: Call<PredictResponse>,
                    response: Response<PredictResponse>
                ) {
                    predictionModel.postValue(response.body()?.let { DataMapper.mapPredictToPredictModel(it)})
                }

                override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                    Log.d("Debug:", "retrofit error: $t")
                }
            })
    }

    fun getAllNews() {
        apiRequest.getAllNews("lung", "en", API_KEY_NEWS)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    news.postValue(response.body()?.articles)
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.d("Debug:", "retrofit error: $t")
                }

            })
    }

    fun getAirQuality(lat: Double, lon: Double) {
        apiRequestAirQuality.getAirQuality(lat, lon, API_KEY_AQI)
            .enqueue(object : Callback<AirQualityResponse> {
                override fun onResponse(
                    call: Call<AirQualityResponse>,
                    response: Response<AirQualityResponse>
                ) {
                    airQuality.postValue(response.body()?.data?.get(0))
                }

                override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                    Log.d("Debug:", "retrofit error: $t")
                }

            })
    }

    fun loginToApp(email: String, password: String) {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { taskId ->
            if (taskId.isSuccessful) {
                getUserInfo()
                registerSuccess.postValue(StatusProses(true, "Login Success"))
            } else {
                val message = taskId.exception?.localizedMessage.toString()
                registerSuccess.postValue(
                    StatusProses(
                        false,
                        message
                    )
                )
                FirebaseAuth.getInstance().signOut()
            }
        }

    }

    fun getUserInfo() {
        val userRef = FirebaseDatabase.getInstance(BUCKET_LINK).reference.child("Users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(UserModel::class.java)
                typeUser.postValue(data?.type)
                dataUser.postValue(data)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun registerToApp(fullName: String, email: String, password: String, type: String) {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { taskId ->
                if (taskId.isSuccessful) {
                    saveUserInfo(fullName, email, type)
                } else {
                    val message = taskId.exception?.localizedMessage.toString()
                    registerSuccess.postValue(
                        StatusProses(
                            false,
                            message
                        )
                    )
                    mAuth.signOut()
                }
            }
    }

    private fun saveUserInfo(fullName: String, email: String, type: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef: DatabaseReference =
            FirebaseDatabase.getInstance(BUCKET_LINK).reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["fullname"] = fullName
        userMap["email"] = email
        userMap["type"] = type

        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { taskId ->
                if (taskId.isSuccessful) {
                    typeUser.postValue(type)
                    registerSuccess.postValue(StatusProses(true, "Account has been created"))
                } else {
                    val message = taskId.exception!!.toString()
                    registerSuccess.postValue(
                        StatusProses(
                            false,
                            message
                        )
                    )
                    FirebaseAuth.getInstance().signOut()
                }
            }
    }

    fun checkEmailPatient(email: String) {
        //check email patient exists
        val reff =
            FirebaseDatabase.getInstance(BUCKET_LINK).reference.child(
                "Users"
            )
        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var i = false
                snapshot.children.forEach { data ->
                    if (data.child("email").getValue(String::class.java).equals(email)) {
                        dataUser.postValue(data.getValue(UserModel::class.java))
                        i = true
                    } else {
                        if (!i) {
                            dataUser.postValue(null)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDataTreatment(diagnose: String, confidence: String, note: String, user: UserModel, dataDoctor: UserModel) {
        val treatmentMap = HashMap<String, Any>()
        val reff =
            FirebaseDatabase.getInstance(BUCKET_LINK).reference.child(
                "Treatment"
            ).child("Patients").child(
                user.uid.toString()
            ).child("History")
        val treatmentId = reff.push().key.toString()
        treatmentMap["treatmentId"] = treatmentId
        treatmentMap["patientId"] = user.uid.toString()
        treatmentMap["diagnose"] = diagnose
        treatmentMap["confidence"] = confidence
        treatmentMap["note"] = note
        treatmentMap["doctorId"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
        treatmentMap["date"] = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).toString()
        treatmentMap["namePatient"] = user.fullname.toString()
        treatmentMap["nameDoctor"] = dataDoctor.fullname.toString()

        reff.child(treatmentId).updateChildren(treatmentMap)

        val treatmentMap2 = HashMap<String, Any>()
        val reff2 =
            FirebaseDatabase.getInstance(BUCKET_LINK).reference.child(
                "Treatment"
            ).child("Doctor").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child("History")
        treatmentMap2["treatmentId"] = treatmentId
        treatmentMap2["patientId"] = user.uid.toString()
        treatmentMap2["diagnose"] = diagnose
        treatmentMap2["confidence"] = confidence
        treatmentMap2["note"] = note
        treatmentMap2["doctorId"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
        treatmentMap2["date"] = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).toString()
        treatmentMap2["namePatient"] = user.fullname.toString()
        treatmentMap2["nameDoctor"] = dataDoctor.fullname.toString()

        reff2.child(treatmentId).updateChildren(treatmentMap2)

        saveDataTreatment.postValue(StatusProses(true, "Send notes treatment success!"))

    }

    fun getAllTreatment(userId: String, type: String) {
        //check email patient exists
        val reff: DatabaseReference = if (type == "doctor") {
            FirebaseDatabase.getInstance(BUCKET_LINK).reference.child(
                "Treatment"
            ).child("Doctor").child(userId).child("History")
        } else {
            FirebaseDatabase.getInstance(BUCKET_LINK).reference.child(
                "Treatment"
            ).child("Patients").child(userId).child("History")
        }
        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listData = ArrayList<TreatmentModel>()
                    snapshot.children.forEach { data ->
                        data.getValue(TreatmentModel::class.java)?.let { listData.add(it) }
                    }
                    dataTreatment.postValue(listData)
                } else {
                    dataTreatment.postValue(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}