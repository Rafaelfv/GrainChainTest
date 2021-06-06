package com.rafaelfv.grainchaintest.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.rafaelfv.grainchaintest.R

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

}