package com.rafaelfv.grainchaintest.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.ui.dialogs.DialogPermission
import com.rafaelfv.grainchaintest.utils.*


class FragmentMain : Fragment(), OnMapReadyCallback {


    private lateinit var permissionDialog: DialogPermission
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var myLocation: Location? = null
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        permissionDialog =
            DialogPermission(requireContext(), object : DialogPermission.DialogPermissionType {
                override fun showAskPermission(permission: PermissionType) {
                    askPermission(permission)
                    permissionDialog.dismiss()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmemt_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapa_address) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10
        locationRequest.fastestInterval = 10
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d(TAG, "onMapReady: true")
        googleMap = p0
        showCurrentPosition()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        try {
            if (Manifest.permission.ACCESS_FINE_LOCATION.isPermissionOk(requireContext())) {
                Log.d(TAG, "getDeviceLocation: Permmisionok")
                if (isGpsEnabled(requireContext())) {
                    try {
                        if (Manifest.permission.ACCESS_FINE_LOCATION.isPermissionOk(requireContext())) {
                            val task = fusedLocationProviderClient.lastLocation
                            task.addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    if (result.result != null) {
                                        myLocation = result.result
                                        val ubicacionLatLng = LatLng(myLocation?.latitude ?: 19.434381,
                                            myLocation?.longitude ?: -99.142651)
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionLatLng, ZOOM_MAP))
                                    }
                                } else {
                                    val ubicacionLatLng = LatLng(19.434381, -99.142651)
                                    googleMap.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            ubicacionLatLng,
                                            12f
                                        )
                                    )
                                    googleMap.uiSettings.isMyLocationButtonEnabled = false
                                }
                            }
                        } else {
                            permissionDialog.show()
                        }
                    } catch (e: Exception) {

                    }
                } else {
                    Log.d(TAG, "getDeviceLocation: Gps Not Enabled")
                    displayLocationSettingsRequest(requireContext())
                }
            } else {
                showDialogPermission()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun showCurrentPosition() {
        updateLocationUI()
        getDeviceLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (Manifest.permission.ACCESS_FINE_LOCATION.isPermissionOk(requireContext())) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMapToolbarEnabled = true
            } else {
                googleMap.isMyLocationEnabled = false
                googleMap.uiSettings.isMapToolbarEnabled = false
                permissionDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.Location -> {
                askForPermission(
                    activity = activity as AppCompatActivity,
                    permission = Manifest.permission.ACCESS_FINE_LOCATION,
                    codeRequest = REQUEST_CODE_PERMISSION_LOCATION
                )
            }
        }
    }

    fun showDialogPermission() {
        if (!Manifest.permission.ACCESS_FINE_LOCATION.isPermissionOk(requireContext())) {
            permissionDialog.show()
            permissionDialog.updateText(PermissionType.Location)
        }
    }


    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> Log.i(TAG, "All location settings are satisfied.")
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ")
                    try {
                        status.startResolutionForResult(requireActivity(), REQUEST_CODE_LOCATION_TRIGGER)
                    } catch (e: SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }
    }
}