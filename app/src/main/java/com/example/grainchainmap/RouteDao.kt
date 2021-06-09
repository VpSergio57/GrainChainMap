package com.example.grainchainmap

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.grainchainmap.placeholder.RouteEntity

@Dao
interface RouteDao {

    @Query("SELECT * FROM RouteEntity")
    fun getAllRoutes():ArrayList<RouteEntity>

    @Insert
    fun addRoute(routeEntity: RouteEntity)

    @Delete
    fun deleteroute(routeEntity: RouteEntity)
}