package com.rafaelfv.grainchaintest.viewmodels

import androidx.fragment.app.Fragment
import com.rafaelfv.grainchaintest.base.BaseViewModel
import com.rafaelfv.grainchaintest.ui.fragments.FragmentMain

class MainActivityViewModel : BaseViewModel() {

    private var fragmentMain: FragmentMain = FragmentMain()


    fun getFragmentMain(): Fragment {
        return fragmentMain
    }
}