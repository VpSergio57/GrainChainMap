package com.example.grainchainmap.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.grainchainmap.domain.entities.RutaEntity

@Dao
interface RouteDao {

    @Query("SELECT * FROM RouteEntity")
    fun getAllRoutes():MutableList<RutaEntity>

    @Insert
    fun addRoute(routeEntity: RutaEntity): Long

    @Delete
    fun deleteRoute(routeEntity: RutaEntity): Long
}