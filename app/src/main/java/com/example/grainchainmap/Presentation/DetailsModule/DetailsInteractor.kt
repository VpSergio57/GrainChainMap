package com.example.grainchainmap.Presentation.DetailsModule

import com.example.grainchainmap.GrainChainMapApplication
import com.example.grainchainmap.Presentation.MapsModule.MapInteractor
import com.example.grainchainmap.domain.entities.RutaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsInteractor {


    val scope = CoroutineScope(Dispatchers.IO)

    fun deleteRouteToDB(route: RutaEntity, callback: (Boolean) -> Unit){
        scope.launch {
            GrainChainMapApplication.database.routeDao().deleteRoute(route)
            withContext(Dispatchers.Main){
                callback(true)
            }
        }
    }

}