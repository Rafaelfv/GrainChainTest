package com.rafaelfv.grainchaintest.ui.fragments

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.data.Dot
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.databinding.ItemRouteBinding
import com.rafaelfv.grainchaintest.utils.getDistance

class AdapterRoute(private val listener: OnItemEvents) :
    RecyclerView.Adapter<AdapterRoute.ItemRouteViewHolder>() {


    open var list: List<Route> = ArrayList()

    interface OnItemEvents {
        fun onItemClick(position: Int, route: Route)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRouteViewHolder =
        DataBindingUtil.inflate<ItemRouteBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_route,
            parent,
            false
        ).let { ItemRouteViewHolder(it) }

    override fun onBindViewHolder(holder: ItemRouteViewHolder, position: Int) {
        holder.bind(list[position])
        holder.container.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: Onclick")
            listener.onItemClick(position, list[position])
        }

    }

    override fun getItemCount(): Int =
        list.count()

    inner class ItemRouteViewHolder(private val binding: ItemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val container = binding.containerItemRoute

        fun bind(route: Route) {
            binding.apply {
                this.route = route
                this.distance.text = "Distance: ${calculateDistanceinKm(route.dots)} kms"
                executePendingBindings()
            }
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
    }

}