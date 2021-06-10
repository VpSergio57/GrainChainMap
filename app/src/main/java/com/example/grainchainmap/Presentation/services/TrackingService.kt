package com.example.grainchainmap.Presentation.services
import android.annotation.SuppressLint
import com.example.grainchainmap.utils.Constants.ACTION_START_OR_RESUME_SERVICE


import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.grainchainmap.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.grainchainmap.utils.Constants.ACTION_STOP_SERVICE
import com.example.grainchainmap.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.example.grainchainmap.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.example.grainchainmap.utils.Permissions
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class TrackingService: LifecycleService() {

    private var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val pathPoint = MutableLiveData<MutableList<LatLng>>()
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoint.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, {
            updateLocationTracking(it)
        })



    }

    private fun pauseService(){
        isTracking.postValue(false)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let{
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE ->{
                    if(isFirstRun){
                        startForegroundService()
                        Log.d("SER_SERVICE","Started service")
                    }else{
                        Log.d("SER_SERVICE","Resumed service")
                    }

                }
                ACTION_PAUSE_SERVICE ->{
                    pauseService()
                    Log.d("SER_SERVICE","Paused service")
                }
                ACTION_STOP_SERVICE ->{
                    Log.d("SER_SERVICE","Stopped Service")
                }

                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        addEmptyPolyline()
        isTracking.postValue(true)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking:Boolean){
        if(isTracking){
            if(Permissions.hasLocationPermission(this)){
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            }
        } else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations?.let { locations ->
                    for (location in locations){
                        addPathPoint(location)
                        Log.d("SER_SERVICE", "${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }
    private fun addPathPoint(location: Location?){
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)

            pathPoint.value?.apply {
                add(pos)
                pathPoint.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoint.postValue(mutableListOf())

}