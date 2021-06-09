package com.rafaelfv.grainchaintest.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.ui.fragments.AdapterRoute
import com.rafaelfv.grainchaintest.ui.fragments.FragmentRoutes.Companion.adapterRoute

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibility")
    fun setVisibility(view: View, visibility: Boolean = false) {
        if (visibility) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }


    @JvmStatic
    @BindingAdapter("animPulse")
    fun startAnimating(view: View, animate: Boolean) {
        YoYo.with(Techniques.Pulse).repeat(YoYo.INFINITE).playOn(view)
    }

    @JvmStatic
    @BindingAdapter("imageSrc")
    fun setImageResource(view: ImageView, idImage: Int) {
        val id = R.mipmap.ic_stop_record
        view.setImageResource(idImage)
    }

    @JvmStatic
    @BindingAdapter("adapterItems")
    fun setItems(view: RecyclerView, items: ArrayList<Route>?) {
        view.adapter = items?.let {
            adapterRoute.apply {
                list = items
                notifyDataSetChanged()
            }
        }
    }
}