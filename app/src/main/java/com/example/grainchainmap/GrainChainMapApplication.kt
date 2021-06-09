package com.example.grainchainmap

import android.app.Application
import androidx.room.Room

class GrainChainMapApplication:Application() {

    companion object{
        lateinit var database: GrainChainMapDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            GrainChainMapDatabase::class.java,
            "GrainChainMapDB")
        .build()
    }


}