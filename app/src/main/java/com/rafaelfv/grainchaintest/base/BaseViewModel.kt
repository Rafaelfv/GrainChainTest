package com.rafaelfv.grainchaintest.base

import androidx.lifecycle.ViewModel
import com.rafaelfv.grainchaintest.MyApplication
import com.rafaelfv.grainchaintest.viewmodels.FragmentMainViewModel
import com.rafaelfv.grainchaintest.viewmodels.FragmentRouteDetailViewModel
import com.rafaelfv.grainchaintest.viewmodels.FragmentRoutesViewModel

open class BaseViewModel : ViewModel() {


    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is FragmentMainViewModel -> MyApplication.component.inject(this)
            is FragmentRoutesViewModel -> MyApplication.component.inject(this)
            is FragmentRouteDetailViewModel -> MyApplication.component.inject(this)
        }
    }

}