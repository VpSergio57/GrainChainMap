package com.example.grainchainmap.utils

import androidx.room.TypeConverter
import com.example.grainchainmap.domain.LatLngData
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun jsonToArraylisy(value: String): List<LatLngData> = Gson().fromJson( value, Array<LatLngData>::class.java).toList()

    @TypeConverter
    fun arraylistToJson(value: MutableList<LatLngData>?) = Gson().toJson(value)

}