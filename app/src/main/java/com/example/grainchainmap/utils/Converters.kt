package com.example.grainchainmap.utils

import androidx.room.TypeConverter
import com.example.grainchainmap.placeholder.RouteEntity
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun arraylistToJson(value: ArrayList<RouteEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToArraylisy(value: String) = Gson().fromJson( value, Array<RouteEntity>::class.java).toList()
}