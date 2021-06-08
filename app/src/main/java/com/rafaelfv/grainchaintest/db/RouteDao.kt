package com.rafaelfv.grainchaintest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rafaelfv.grainchaintest.data.Dot
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.data.RouteInfo

@Dao
abstract class RouteDao {

    @Insert
    abstract suspend fun insertDots(listDots: List<Dot>)

    @Insert
    abstract suspend fun insertRouteInfo(routeInfo: RouteInfo)

    @Transaction
    @Query("SELECT *FROM RouteInfo WHERE id IN (:id)")
    abstract suspend fun getRouteById(id: Long): List<Route>

}