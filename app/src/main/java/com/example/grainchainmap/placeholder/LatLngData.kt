package com.example.grainchainmap.placeholder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatLngData(
    var latitude:Double,
    var longitude:Double
):Parcelable
