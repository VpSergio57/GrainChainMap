package com.example.grainchainmap.Presentation.services
import com.example.grainchainmap.utils.Constants.ACTION_START_OR_RESUME_SERVICE


import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.example.grainchainmap.utils.Constants
import com.example.grainchainmap.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.grainchainmap.utils.Constants.ACTION_STOP_SERVICE

class TrackingService: LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let{
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE ->{
                    Log.d("SERVICE","Started or resumed service")
                }
                ACTION_PAUSE_SERVICE ->{
                    Log.d("SERVICE","Paused service")
                }
                ACTION_STOP_SERVICE ->{
                    Log.d("SERVICE","Stopped Service")
                }

                else -> {

                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

}