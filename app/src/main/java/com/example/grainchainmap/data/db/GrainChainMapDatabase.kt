package com.example.grainchainmap.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.grainchainmap.domain.entities.RutaEntity
import com.example.grainchainmap.utils.Converters

@Database(entities = arrayOf(RutaEntity::class), version = 2)
@TypeConverters(Converters::class)
abstract class GrainChainMapDatabase:RoomDatabase() {

    abstract fun routeDao(): RouteDao

}