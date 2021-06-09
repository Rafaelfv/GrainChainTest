package com.rafaelfv.grainchaintest.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import com.rafaelfv.grainchaintest.base.BaseViewModel
import com.rafaelfv.grainchaintest.ui.fragments.FragmentMain
import com.rafaelfv.grainchaintest.ui.fragments.FragmentRoutes
import kotlin.math.log

class MainActivityViewModel : BaseViewModel() {

    private var fragmentMain: FragmentMain = FragmentMain()
    private var fragmentRoutes: FragmentRoutes = FragmentRoutes()

    fun getFragmentMain(): Fragment {
        return fragmentMain
    }

    fun getFragmentRoutes(): Fragment {
        return fragmentRoutes
    }

    fun refreshData() {
        fragmentRoutes.refreshData()
    }

}