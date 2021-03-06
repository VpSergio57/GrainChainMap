package com.example.grainchainmap.Presentation.services
import android.annotation.SuppressLint
import com.example.grainchainmap.utils.Constants.ACTION_START_OR_RESUME_SERVICE


import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.grainchainmap.utils.Constants.ACTION_STOP_SERVICE
import com.example.grainchainmap.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.example.grainchainmap.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.example.grainchainmap.utils.Constants.TIME_UDATE_INTERVAL
import com.example.grainchainmap.utils.Permissions
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackingService: LifecycleService() {

    private var serviceKilled = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isTimerEnabled = false
    private var timeRun = 0L
    private var timeStarted = 0L

    companion object{
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoint = MutableLiveData<MutableList<LatLng>>()
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoint.postValue(mutableListOf())
        timeRunInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let{
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE ->{
                    startForegroundService()
                    Log.d("SER_SERVICE","Started service")
                }
                ACTION_STOP_SERVICE ->{
                    killService()
                    Log.d("SER_SERVICE","Stopped Service")
                }
                else -> { }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        startTimer()
        postInitialValues()
        isTracking.postValue(true)
    }

    private fun killService(){
        isTracking.postValue(false)
        isTimerEnabled = false
        serviceKilled = true
        stopSelf()
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

    private val locationCallback = object: LocationCallback() {
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

    private fun startTimer(){
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                timeRun = System.currentTimeMillis() - timeStarted
                timeRunInMillis.postValue(timeRun)
                delay(TIME_UDATE_INTERVAL)
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
}