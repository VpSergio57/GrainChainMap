package com.example.grainchainmap.domain.entities


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.grainchainmap.domain.LatLngData
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity( tableName = "RouteEntity", indices = [Index(value = ["id"], unique = true)] )
data class RutaEntity(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    var name:String = "",
    var km:Float = 0f,
    var time: String = "", //Aun no se si se calcular'a despues y no por la api
    var latlongList: MutableList<LatLngData>
): Parcelable
