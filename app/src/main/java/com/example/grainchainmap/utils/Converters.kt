package com.example.grainchainmap.utils

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun jsonToArraylisy(value: String): List<LatLng> = Gson().fromJson( value, Array<LatLng>::class.java).toList()

    @TypeConverter
    fun arraylistToJson(value: MutableList<LatLng>?) = Gson().toJson(value)

}