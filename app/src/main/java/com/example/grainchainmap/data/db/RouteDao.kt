package com.example.grainchainmap.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.grainchainmap.placeholder.Route

@Dao
interface RouteDao {

    @Query("SELECT * FROM RouteEntity")
    fun getAllRoutes():MutableList<Route>

    @Insert
    fun addRoute(routeEntity: Route)

    @Delete
    fun deleteroute(routeEntity: Route)
}