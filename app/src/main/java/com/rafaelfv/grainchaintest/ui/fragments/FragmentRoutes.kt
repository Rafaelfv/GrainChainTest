package com.rafaelfv.grainchaintest.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.data.Route
import com.rafaelfv.grainchaintest.databinding.FragmentListRoutesBinding
import com.rafaelfv.grainchaintest.utils.FRAGMENT_TAG_ROUTE_DETAIL
import com.rafaelfv.grainchaintest.utils.KEY_ROUTE
import com.rafaelfv.grainchaintest.utils.addFragment
import com.rafaelfv.grainchaintest.viewmodels.FragmentRouteDetailViewModel
import com.rafaelfv.grainchaintest.viewmodels.FragmentRoutesViewModel

class FragmentRoutes : Fragment() {

    private val viewModel: FragmentRoutesViewModel by viewModels()

    companion object {
        lateinit var adapterRoute: AdapterRoute
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterRoute = AdapterRoute(object : AdapterRoute.OnItemEvents {
            override fun onItemClick(position: Int, route: Route) {
                val bundle  = Bundle()
                bundle.putParcelable(KEY_ROUTE, route)
                val fragmentRouteDetail = FragmentRouteDetail()
                fragmentRouteDetail.arguments = bundle
                requireActivity().supportFragmentManager.addFragment(fragmentRouteDetail, R.id.container_main_activity, FRAGMENT_TAG_ROUTE_DETAIL)
            }
        })
    }

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listRoutes.observe(viewLifecycleOwner, Observer {
            adapterRoute.list = it
            adapterRoute.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
    }

    fun onRemoveRoute(route: Route) {
        Log.d(TAG, "refreshData: Routess")
        adapterRoute.list.remove(route)
        adapterRoute.notifyDataSetChanged()
    }
}