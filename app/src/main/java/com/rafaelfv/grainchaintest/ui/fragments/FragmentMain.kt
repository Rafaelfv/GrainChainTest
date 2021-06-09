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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.databinding.FragmemtMainBinding
import com.rafaelfv.grainchaintest.ui.dialogs.DialogNameRoute
import com.rafaelfv.grainchaintest.ui.dialogs.DialogPermission
import com.rafaelfv.grainchaintest.utils.*
import com.rafaelfv.grainchaintest.viewmodels.FragmentMainViewModel
import com.rafaelfv.grainchaintest.utils.addFragment
import com.rafaelfv.grainchaintest.viewmodels.MainActivityViewModel


class FragmentMain : Fragment(), OnMapReadyCallback {

    private lateinit var permissionDialog: DialogPermission
    private lateinit var dialogNameRoute: DialogNameRoute
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var myLocation: Location? = null
    private lateinit var locationCallback: LocationCallback
    private val viewModel: FragmentMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        dialogNameRoute = DialogNameRoute(requireContext(), object : DialogNameRoute.DialogEvent {
            override fun saveRoute(name: String) {
                dialogNameRoute.dismiss()
                googleMap.clear()
                viewModel.saveRoute(name)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.route_saved_ok),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun cancel() {
                dialogNameRoute.dismiss()
                viewModel.cleanListLatLong()
                googleMap.clear()
            }

        })

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
    ): View {

        val viewDataBinding: FragmemtMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragmemt_main, container, false)
        viewDataBinding.lifecycleOwner = requireActivity()
        val view = viewDataBinding.root
        viewDataBinding.viewModel = viewModel
        return view
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
                for (location in locationResult.locations) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    viewModel.addLatLong(latLng)
                }
            }
        }

        val recordingObserver = Observer<Boolean> { recording ->
            if (recording) {
                startLocationUpdates()
            } else {
                stopLocationUpdates()
                dialogNameRoute.show()
            }
        }

        val markerObserver = Observer<MarkerOptions> { marker ->
            googleMap.addMarker(marker).showInfoWindow()
        }

        val fragmentToAddObserver = Observer<Fragment> { fragment ->
            val viewModelMainactivity: MainActivityViewModel by viewModels()
            requireActivity().supportFragmentManager.addFragment(
                FragmentRoutes(),
                R.id.container_main_activity,
                FRAGMENT_TAG_ROUTES
            )
        }

        viewModel.recording.observe(viewLifecycleOwner, recordingObserver)
        viewModel.marker.observe(viewLifecycleOwner, markerObserver)
        viewModel.fragmentToAdd.observe(viewLifecycleOwner, fragmentToAddObserver)

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = INTERVAL_UPDATE_LOCATION
        locationRequest.fastestInterval = INTERVAL_UPDATE_LOCATION
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        showCurrentPosition()
    }

    fun showCurrentPosition() {
        updateLocationUI()
        getDeviceLocation()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        if (Manifest.permission.ACCESS_FINE_LOCATION.isPermissionOk(requireContext())) {
            if (isGpsEnabled(requireContext())) {
                try {
                    val task = fusedLocationProviderClient.lastLocation
                    task.addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            if (result.result != null) {
                                myLocation = result.result
                                val ubicacionLatLng = LatLng(
                                    myLocation?.latitude ?: 19.434381,
                                    myLocation?.longitude ?: -99.142651
                                )
                                googleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        ubicacionLatLng,
                                        ZOOM_MAP
                                    )
                                )
                                viewModel.updateVisibilityBtn(true)
                            } else {
                                getDeviceLocation()
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
                } catch (e: Exception) {

                }
            } else {
                displayLocationSettingsRequest(requireContext())
            }
        } else {
            showDialogPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
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
                LocationSettingsStatusCodes.SUCCESS -> {
                    getDeviceLocation()
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        status.startResolutionForResult(
                            requireActivity(),
                            REQUEST_CODE_LOCATION_TRIGGER
                        )
                    } catch (e: SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
            }
        }
    }

    private fun addMarker(map: GoogleMap, marker: MarkerOptions) {
        map.addMarker(marker).showInfoWindow()
    }
}