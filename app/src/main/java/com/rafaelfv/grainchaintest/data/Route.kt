package com.rafaelfv.grainchaintest.data

import com.google.android.gms.maps.model.LatLng

data class Route (
    val title: String,
    val listLatLong: List<LatLng>
    )