package com.example.grainchainmap

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.grainchainmap.placeholder.RouteEntity

@Database(entities = arrayOf(RouteEntity::class), version = 1)
abstract class GrainChainMapDatabase:RoomDatabase() {

    abstract fun routeDao(): RouteDao

}