package com.lunion.lunionapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.lunion.lunionapp.databinding.FragmentAirQualityBinding
import com.lunion.lunionapp.viewmodel.AirQualityViewModel
import com.lunion.lunionapp.viewmodel.ViewModelFactory
import java.util.*

class AirQualityFragment : Fragment() {

    private lateinit var binding: FragmentAirQualityBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: AirQualityViewModel
    private val permissionId = 1010

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAirQualityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //loader
        checkIsLoading(false)

        //viewModel
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[AirQualityViewModel::class.java]

        //observeLiveData
        viewModel.airQuality.observe(viewLifecycleOwner,{
            binding.resultNumberAqi.text = it.aqi.toString()
            binding.resultTypeAqi.text = rangeAqi(it.aqi)
            showVisibilityAirQuality()
            checkIsLoading(false)
        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Log.d("Debug:","Hallo")
        binding.btnScan.setOnClickListener {
            checkIsLoading(true)
            Log.d("Debug:","cek "+checkPermission().toString())
            Log.d("Debug:", "cek " + isLocationEnabled().toString())
            requestPermission()
            getLastLocation()
        }
    }

    private fun checkPermission():Boolean{
        if(
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    private fun requestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            permissionId
        )
    }


    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    private fun getLastLocation(){
        Log.d("Debug:","getlastlocation")
        if(checkPermission()){
            Log.d("Debug:","masuk1")
            if(isLocationEnabled()){
                Log.d("Debug:","masuk2")
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                        Log.d("Debug:","masuk3")
                        val location: Location? = task.result
                        if(location == null){
                            newLocationData()
                        }else{
                            Log.d("Debug:" ,"Your Location:"+ location.longitude)
                            binding.location.text = getCityName(location.latitude,location.longitude)
                            binding.latLon.text = "Lat: ${location.latitude} Lon: ${location.longitude}"
                            viewModel.getAirQuality(location.latitude, location.longitude)
                        }
                    }
            }else{
                Toast.makeText(requireContext(),"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
                checkIsLoading(false)
            }
        }else{
            requestPermission()
        }
    }


    private fun newLocationData(){
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug:","masuk4")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
            )
        }

    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            binding.location.text = getCityName(lastLocation.latitude,lastLocation.longitude)
            binding.latLon.text = "Lat: ${lastLocation.latitude} Lon: ${lastLocation.longitude}"
            viewModel.getAirQuality(lastLocation.latitude, lastLocation.longitude)
        }
    }


    private fun getCityName(lat: Double,long: Double):String{
        val cityName:String
        val countryName: String
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,long,3)
        cityName = address[0].locality
        countryName = address[0].countryName
        Log.d("Debug:", "Your City: $cityName ; your Country $countryName")
        return cityName
    }

    private fun checkIsLoading(data: Boolean) {
        if (data){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showVisibilityAirQuality(){
        binding.resultNumberAqi.visibility = View.VISIBLE
        binding.resultTypeAqi.visibility = View.VISIBLE
        binding.location.visibility = View.VISIBLE
        binding.latLon.visibility = View.VISIBLE
    }

    private fun rangeAqi(aqi: Int): String{
        when {
            aqi<=50 -> {
                return "HEALTHY AIR"
            }
            aqi<=100 -> {
                return "MODERATE AIR"
            }
            aqi<=150 -> {
                return "BAD FOR VULNERABLE PEOPLE"
            }
            aqi<=200 -> {
                return "AIR IS NOT HEALTHY"
            }
            aqi<=250 -> {
                return "AIR IS VERY UNHEALTHY"
            }
            aqi<=300 -> {
                return "HAZARDOUS AIR"
            }else ->{
                return "CAN'T DETECT"
            }
        }
    }

}