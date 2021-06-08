package com.example.grainchainmap.utils

import android.content.Context
import android.Manifest
import android.os.Build
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions
import com.example.grainchainmap.utils.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.example.grainchainmap.utils.Constants.PERMISSION_LOCATION_REQUEST_CODE

object Permissions {

    fun hasLocationPermission(context: Context) = EasyPermissions.hasPermissions(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun requestLocationPermission(fragment:Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "La Aplicacion necesita obtener tu ubicacion para funcionar",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun hasBackgroundLocationPermission(context:Context):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

        return true
    }

    fun requestBackgroundLocationPermission(fragment:Fragment){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                fragment,
                "La App necesita obtener la ubicacion en Background",
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

}