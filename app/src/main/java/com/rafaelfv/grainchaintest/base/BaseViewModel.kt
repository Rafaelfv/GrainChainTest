package com.rafaelfv.grainchaintest.base

import androidx.lifecycle.ViewModel
import com.rafaelfv.grainchaintest.MyApplication
import com.rafaelfv.grainchaintest.viewmodels.FragmentMainViewModel

open class BaseViewModel : ViewModel() {


    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is FragmentMainViewModel -> MyApplication.component.inject(this)
        }
    }

}