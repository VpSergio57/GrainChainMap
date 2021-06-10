package com.example.grainchainmap.Presentation.MapsModule

import com.example.grainchainmap.GrainChainMapApplication
import com.example.grainchainmap.domain.entities.RutaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.security.auth.callback.Callback

class MapInteractor {

    val scope = CoroutineScope(Dispatchers.IO)

    fun getRoutesFromDB(callback: (MutableList<RutaEntity>) ->Unit){
        scope.launch {
            val respose = GrainChainMapApplication.database.routeDao().getAllRoutes()
            callback(respose)
        }
    }

    fun addRouteToDB(route:RutaEntity, callback: (Boolean) -> Unit){
        scope.launch {
            GrainChainMapApplication.database.routeDao().addRoute(route)
            callback(true)
        }
    }


}