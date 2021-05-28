package com.lunion.lunionapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import com.google.android.gms.location.*
import com.lunion.lunionapp.databinding.FragmentAirQualityBinding
import java.util.*

class AirQualityFragment : Fragment() {

    private lateinit var binding: FragmentAirQualityBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Log.d("Debug:","Hallo")
        binding.btnScan.setOnClickListener {
            Log.d("Debug:","cek "+checkPermission().toString())
            Log.d("Debug:", "cek " + isLocationEnabled().toString())
            requestPermission()
            /* fusedLocationProviderClient.lastLocation.addOnSuccessListener{location: Location? ->
                 textView.text = location?.latitude.toString() + "," + location?.longitude.toString()
             }*/
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
                        }
                    }

            }else{
                Toast.makeText(requireContext(),"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
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



}