package com.rafaelfv.grainchaintest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.databinding.FragmentListRoutesBinding
import com.rafaelfv.grainchaintest.viewmodels.FragmentRoutesViewModel

class FragmentRoutes: Fragment() {

    private val viewModel: FragmentRoutesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewDataBinding: FragmentListRoutesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_routes, container, false)
        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = requireActivity()
        val view = viewDataBinding.root
        return view
    }
}