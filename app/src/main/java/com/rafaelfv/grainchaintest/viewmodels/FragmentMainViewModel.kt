package com.rafaelfv.grainchaintest.viewmodels

import android.content.Context
import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.base.BaseViewModel
import com.rafaelfv.grainchaintest.db.AppDataBase
import com.rafaelfv.grainchaintest.repository.RouteRepository
import com.rafaelfv.grainchaintest.ui.fragments.FragmentRoutes
import com.rafaelfv.grainchaintest.utils.MIN_DISTANCE_LOCATION
import javax.inject.Inject

class FragmentMainViewModel : BaseViewModel(), LifecycleObserver {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var database: AppDataBase

    var repository: RouteRepository

    var timeStart: Long = 0
    var timeEnd: Long = 0

    var visibilityBtnRecord: MutableLiveData<Boolean> = MutableLiveData()
    var visibilityBtnRoutesSaved: MutableLiveData<Boolean> = MutableLiveData()
    var visibilityBtnIndicator: MutableLiveData<Boolean> = MutableLiveData()
    var recording: MutableLiveData<Boolean> = MutableLiveData()
    var marker: MutableLiveData<MarkerOptions> = MutableLiveData()
    var srcImageRecording: MutableLiveData<Int> = MutableLiveData()
    var fragmentToAdd: MutableLiveData<Fragment> = MutableLiveData()
    var listLatLong: ArrayList<LatLng> = ArrayList()

    private var recordingLastStatus = false

    init {
        repository = RouteRepository(database.routeDao(), viewModelScope)
        srcImageRecording.value = R.mipmap.ic_route_map
        visibilityBtnRoutesSaved.value = repository.isAtLeastOneRouteSaved()
    }

    fun updateVisibilityBtn(visible: Boolean) {
        visibilityBtnRecord.value = visible
    }

    fun openFragmentRoutes() {
        fragmentToAdd.value = FragmentRoutes()
    }

    fun onClickBtnRecord() {
        if (!recordingLastStatus) {
            startRecording()
        } else {
            stopRecording()
        }
        recordingLastStatus = !recordingLastStatus
    }

    private fun stopRecording() {
        timeEnd = System.currentTimeMillis()
        visibilityBtnIndicator.value = false
        srcImageRecording.value = R.mipmap.ic_route_map
        recording.value = false
    }

    private fun startRecording() {
        timeStart = System.currentTimeMillis()
        visibilityBtnIndicator.value = true
        recording.value = true
        srcImageRecording.value = R.mipmap.ic_stop_record
    }

    fun addLatLong(latLng: LatLng) {
        if (listLatLong.isEmpty()) {
            val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.location_2)
            this.marker.value = MarkerOptions().position(latLng).icon(icon)
            listLatLong.add(latLng)
            return
        }

        val calculateDistance = { latLongStart: LatLng, latLongEnd: LatLng ->
            val results = FloatArray(2)
            Location.distanceBetween(
                latLongStart.latitude,
                latLongStart.longitude,
                latLongEnd.latitude,
                latLongEnd.longitude,
                results
            )
            results[0]
        }

        val distance = calculateDistance(listLatLong.last(), latLng)

        if (distance > MIN_DISTANCE_LOCATION) {
            val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.dot3)
            this.marker.value = MarkerOptions().position(latLng).icon(icon)
            listLatLong.add(latLng)
        }
    }

    fun cleanListLatLong() {
        listLatLong.clear()
    }

    fun saveRoute(name: String) {
        repository.insertRouteInfo(name, timeStart, timeEnd)
        val lastId = repository.getLastRouteInfoId()
        lastId?.let { repository.insertDots(listLatLong, it) }
        visibilityBtnRoutesSaved.value = repository.isAtLeastOneRouteSaved()
    }

}