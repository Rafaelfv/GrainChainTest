package com.rafaelfv.grainchaintest.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.base.BaseViewModel
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.db.AppDataBase
import com.rafaelfv.grainchaintest.repository.RouteRepository
import javax.inject.Inject

class FragmentRouteDetailViewModel : BaseViewModel() {

    @Inject
    lateinit var database: AppDataBase

    @Inject
    lateinit var context: Context

    var repository: RouteRepository

    val nameRoute: MutableLiveData<String> = MutableLiveData()
    val distance: MutableLiveData<String> = MutableLiveData()
    val time: MutableLiveData<String> = MutableLiveData()
    val route: MutableLiveData<Route> = MutableLiveData()
    val onRemoveRoute: MutableLiveData<Any> = MutableLiveData()

    init {
        repository = RouteRepository(database.routeDao(), viewModelScope)
    }

    fun setRoute(route: Route?){
        this.route.value = route
    }

    fun setTime(time: String){
        this.time.value = time
    }

    fun setName(name: String) {
        nameRoute.value = name
    }

    fun setDistance(distance: String) {
        this.distance.value = distance
    }

    fun deleteRoute(route: Route?) {
        route?.let { repository.deleteRoute(it) }
        Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
        onRemoveRoute.value = true
    }
}