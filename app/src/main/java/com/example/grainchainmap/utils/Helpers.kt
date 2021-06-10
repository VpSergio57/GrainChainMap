package com.example.grainchainmap.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

object Helpers {

    fun getFoormatteStopWachTime(ms:Long, includeMillis:Boolean = false):String{
        var milliSeconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
        milliSeconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)

        if(!includeMillis){
            return "${if(hours<10) "0" else ""}$hours:" +
                    "${if(minutes< 10) "0" else ""}$minutes:" +
                    "${if(seconds<10) "0" else ""}$seconds"
        }

        milliSeconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliSeconds /= 10

        return "${if(hours<10) "0" else ""}$hours:" +
                    "${if(minutes< 10) "0" else ""}$minutes:" +
                    "${if(seconds<10) "0" else ""}$seconds:" +
                    "${if(milliSeconds<10) "0" else ""}$milliSeconds"

    }

    fun convertMetersToKM(mts:Float):Float{

        val km = mts*0.001f

        val roundKM = Math.round(km * 1000.0f) / 1000.0f

        return roundKM
    }

}