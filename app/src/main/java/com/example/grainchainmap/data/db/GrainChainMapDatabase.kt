package com.example.grainchainmap.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.grainchainmap.placeholder.Route
import com.example.grainchainmap.utils.Converters

@Database(entities = arrayOf(Route::class), version = 1)
@TypeConverters(Converters::class)
abstract class GrainChainMapDatabase:RoomDatabase() {

    abstract fun routeDao(): RouteDao

}