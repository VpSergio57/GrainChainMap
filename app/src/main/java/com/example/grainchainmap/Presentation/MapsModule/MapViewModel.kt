package com.example.grainchainmap.Presentation.MapsModule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grainchainmap.GrainChainMapApplication
import com.example.grainchainmap.domain.entities.RutaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MapViewModel:ViewModel() {

    private var routesList: MutableList<RutaEntity>
    private var interactor:MapInteractor

    init{
        routesList = mutableListOf()
        interactor = MapInteractor()
    }

    fun observeRoutes():LiveData<MutableList<RutaEntity>> = routes

    private val routes: MutableLiveData<MutableList<RutaEntity>> by lazy {
        MutableLiveData<MutableList<RutaEntity>>()
    }

    fun loadRoutes(){
        interactor.getRoutesFromDB {
            routesList = it
            this.routes.postValue(it)
        }
    }

    fun addRoute(route:RutaEntity){
        interactor.addRouteToDB(route) {
            if(it){
                routesList.add(route)
                this.routes.postValue(mutableListOf())
                this.routes.postValue(routesList)
            }
        }
    }

    fun deleteRoute(route:RutaEntity){
        interactor.addRouteToDB(route) {
            if(it){
                routesList.remove(route)

                this.routes.postValue(mutableListOf())
                this.routes.postValue(routesList)
            }
        }
    }


}