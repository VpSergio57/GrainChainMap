package com.example.grainchainmap.utils

import androidx.room.TypeConverter
import com.example.grainchainmap.placeholder.Route
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun arraylistToJson(value: ArrayList<Route>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToArraylisy(value: String) = Gson().fromJson( value, Array<Route>::class.java).toList()
}