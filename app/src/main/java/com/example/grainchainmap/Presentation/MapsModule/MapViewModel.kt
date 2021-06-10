package com.example.grainchainmap.Presentation.MapsModule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grainchainmap.domain.entities.RutaEntity


class MapViewModel:ViewModel() {

   // private var routesList: MutableList<RutaEntity>
    private var interactor:MapInteractor

    init{
      //  routesList = mutableListOf()
        interactor = MapInteractor()
    }

    private val routes: MutableLiveData<MutableList<RutaEntity>> by lazy {
        MutableLiveData<MutableList<RutaEntity>>()
    }

    fun observableRoutes():LiveData<MutableList<RutaEntity>> = routes

    fun loadRoutes(){
        interactor.getRoutesFromDB {
            //routesList = it
            this.routes.postValue(mutableListOf())
            this.routes.postValue(it)
        }
    }

    fun addRoute(route:RutaEntity){
        interactor.addRouteToDB(route) {
            if(it){
                loadRoutes()
                //routesList.add(route)
                ///this.routes.postValue(mutableListOf())

                //this.routes.postValue(routesList)
            }
        }
    }



}