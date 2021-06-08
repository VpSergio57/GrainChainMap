package com.example.grainchainmap.placeholder

data class Route(
    var id:Int,
    var name:String,
    var km:Float,
    var latlong: ArrayList<LatLngData>
)
