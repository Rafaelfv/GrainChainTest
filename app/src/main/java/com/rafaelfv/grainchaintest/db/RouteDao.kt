package com.rafaelfv.grainchaintest.db

import androidx.room.*
import com.rafaelfv.grainchaintest.data.Dot
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.data.RouteInfo

@Dao
abstract class RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDots(listDots: List<Dot>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRouteInfo(routeInfo: RouteInfo)

    @Transaction
    @Query("SELECT *FROM RouteInfo")
    abstract suspend fun getAllRoutes(): List<Route>

    @Query("SELECT * FROM RouteInfo")
    abstract fun getRoutesInfo(): List<RouteInfo>

    @Delete
    abstract fun deleteRouteInfo(routeInfo: RouteInfo)

    @Delete
    abstract fun deleteRouteDots(dots: List<Dot>)

}