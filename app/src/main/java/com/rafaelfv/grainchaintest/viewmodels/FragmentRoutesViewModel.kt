package com.rafaelfv.grainchaintest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfv.grainchaintest.base.BaseViewModel
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.db.AppDataBase
import com.rafaelfv.grainchaintest.repository.RouteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentRoutesViewModel : BaseViewModel() {

    @Inject
    lateinit var database: AppDataBase
    var repository: RouteRepository

    var listRoutes: MutableLiveData<ArrayList<Route>> = MutableLiveData()


    init {
        repository = RouteRepository(database.routeDao(), viewModelScope)
        viewModelScope.launch(Dispatchers.IO) {
            listRoutes.postValue(repository.getRoutes() as ArrayList<Route>)
        }
    }

}