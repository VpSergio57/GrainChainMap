package com.example.grainchainmap.Presentation.DetailsModule

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grainchainmap.Presentation.MapsModule.MapInteractor
import com.example.grainchainmap.domain.entities.RutaEntity

class DetailsViewMovel:ViewModel() {

    private var interactor: DetailsInteractor
    init{
        interactor = DetailsInteractor()
    }

    private val closeByDelete: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun observableOnDeleteRoute(): LiveData<Boolean> = closeByDelete

    fun deleteRoute(route: RutaEntity){
        interactor.deleteRouteToDB(route) {
            if(it){
                closeByDelete.postValue(it)
            }
        }
    }


}