package com.rafaelfv.grainchaintest.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {


    init {
        inject()
    }

    fun inject() {

    }

}