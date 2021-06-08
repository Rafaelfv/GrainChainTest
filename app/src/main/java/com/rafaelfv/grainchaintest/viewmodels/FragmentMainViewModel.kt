package com.rafaelfv.grainchaintest.viewmodels

import android.content.Context
import android.location.Location
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.base.BaseViewModel
import com.rafaelfv.grainchaintest.utils.MIN_DISTANCE_LOCATION
import javax.inject.Inject

class FragmentMainViewModel : BaseViewModel(), LifecycleObserver {

    @Inject
    lateinit var context: Context

    var visibilityBtnRecord: MutableLiveData<Boolean> = MutableLiveData()
    var visibilityBtnIndicator: MutableLiveData<Boolean> = MutableLiveData()
    var recording: MutableLiveData<Boolean> = MutableLiveData()
    var marker: MutableLiveData<MarkerOptions> = MutableLiveData()
    var srcImageRecording: MutableLiveData<Int> = MutableLiveData()
    var listLatLong: ArrayList<LatLng> = ArrayList()


    private var recordingLastStatus = false

    init {
        srcImageRecording.value = R.mipmap.ic_route_map
    }

    fun updateVisibilityBtn(visible: Boolean) {
        visibilityBtnRecord.value = visible
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
        visibilityBtnIndicator.value = false
        srcImageRecording.value = R.mipmap.ic_route_map
        recording.value = false
    }

    private fun startRecording() {
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

        val calculateDistance = { latLongStart : LatLng, latLongEnd: LatLng ->
            val results: FloatArray = FloatArray(2)
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

        if(distance > MIN_DISTANCE_LOCATION ) {
            val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.dot3)
            this.marker.value = MarkerOptions().position(latLng).icon(icon)
            listLatLong.add(latLng)
        }

    }

    fun cleanListLatLong() {
        listLatLong.clear()
    }

    fun saveRoute(name: String) {

    }

}