package com.rafaelfv.grainchaintest.repository

import com.google.android.gms.maps.model.LatLng
import com.rafaelfv.grainchaintest.data.Dot
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.data.RouteInfo
import com.rafaelfv.grainchaintest.db.RouteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteRepository(private val routeDao: RouteDao, private val viewModelScope: CoroutineScope) {

    fun insertDots(listLatLng: List<LatLng>, routeId: Long) {
        val listDots: ArrayList<Dot> = ArrayList()
        listLatLng.map { latLng ->
            listDots.add(
                Dot(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    routeId = routeId,
                    id = null
                )
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            routeDao.insertDots(listDots)
        }
    }

    fun insertRouteInfo(name: String) {
        val routeInfo = RouteInfo(title = name, id = null)
        routeDao.insertRouteInfo(routeInfo)
    }

    fun deleteRoute(route: Route) {
        routeDao.deleteRouteInfo(route.routeInfo)
        routeDao.deleteRouteDots(route.dots)

    }

    fun getLastRouteInfoId(): Long? =
        routeDao.getRoutesInfo().last().id

    fun isAtLeastOneRouteSaved(): Boolean = routeDao.getRoutesInfo().isNotEmpty()

    suspend fun getRoutes(): List<Route> =
        routeDao.getAllRoutes()


}