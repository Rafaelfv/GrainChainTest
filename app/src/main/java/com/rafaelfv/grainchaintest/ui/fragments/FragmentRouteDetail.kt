package com.rafaelfv.grainchaintest.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.data.Dot
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.databinding.FragmentRouteDetailBinding
import com.rafaelfv.grainchaintest.utils.KEY_ROUTE
import com.rafaelfv.grainchaintest.utils.ZOOM_MAP
import com.rafaelfv.grainchaintest.utils.getDistance
import com.rafaelfv.grainchaintest.viewmodels.FragmentRouteDetailViewModel
import com.rafaelfv.grainchaintest.viewmodels.MainActivityViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentRouteDetail : Fragment(), OnMapReadyCallback {

    private val viewModel: FragmentRouteDetailViewModel by viewModels()
    private var route: Route? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        route = arguments?.getParcelable<Route>(KEY_ROUTE) as Route
        viewModel.setRoute(route)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewDataBinding: FragmentRouteDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_route_detail, container, false)
        viewDataBinding.lifecycleOwner = requireActivity()
        val view = viewDataBinding.root
        viewDataBinding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_detail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        route?.routeInfo?.title?.let { viewModel.setName(it) }

        route?.dots?.let { calculateDistanceinKm(it) }
            ?.let { viewModel.setDistance("Distance: $it km") }

        val dateStart = route?.routeInfo?.timeStart?.let { getDateHour(it) }
        val dateEnd = route?.routeInfo?.timeEnd?.let { getDateHour(it) }

        viewModel.setTime("Desde: $dateStart hasta: $dateEnd")

        viewModel.onRemoveRoute.observe(viewLifecycleOwner, Observer {
            val viewModelMain: MainActivityViewModel by viewModels()
            route?.let { it1 -> viewModelMain.onRemoveRoute(it1) }
        })

    }

    private fun getDateHour(time: Long): String? {
        val cal = Calendar.getInstance()
        val timeZone = cal.timeZone
        val format = SimpleDateFormat("hh:mm")
        format.timeZone = timeZone
        return format.format(Date(time))
    }

    private fun calculateDistanceinKm(dots: List<Dot>): String {
        var sum: Double = 0.30
        dots.forEachIndexed { index, dot ->
            if (index + 1 < dots.size) {
                val latLngStart: LatLng = LatLng(dot.latitude, dot.longitude)
                val latLngEnd: LatLng =
                    LatLng(dots[index + 1].latitude, dots[index + 1].longitude)
                sum += getDistance(latLngStart, latLngEnd)
            }
        }
        var filterKms: String? = "%.3f".format(sum / 1000)
        return filterKms.toString()
    }

    override fun onMapReady(p0: GoogleMap) {
        val listMarkers: ArrayList<MarkerOptions> = ArrayList()

        val size: Int? = route?.dots?.size
        route?.dots?.mapIndexed { index, dot ->
            val marker: MarkerOptions
            val latLng: LatLng = LatLng(dot.latitude, dot.longitude)

            if (index == 0) {
                val icon: BitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.location_2)
                marker = MarkerOptions().position(latLng).icon(icon)
                p0.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latLng,
                        ZOOM_MAP
                    )
                )
                listMarkers.add(marker)
            } else if (index == size?.minus(1)) {
                val icon: BitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.destination2)
                marker = MarkerOptions().position(latLng).icon(icon)
                listMarkers.add(marker)
            } else {
                val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.dot3)
                marker = MarkerOptions().position(latLng).icon(icon)
                listMarkers.add(marker)
            }
        }

        listMarkers.forEach {
            p0.addMarker(it).showInfoWindow()
        }
    }


}