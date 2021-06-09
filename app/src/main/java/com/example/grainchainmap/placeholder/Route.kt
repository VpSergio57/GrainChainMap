package com.example.grainchainmap.placeholder

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity( tableName = "RouteEntity" )
data class Route(
    @PrimaryKey(autoGenerate = true) var id:Int = 0,
    var name:String = "",
    var km:Float = 0f,
    var time: String = "", //Aun no se si se calcular'a despues y no por la api
    var latlongList: String
): Parcelable
