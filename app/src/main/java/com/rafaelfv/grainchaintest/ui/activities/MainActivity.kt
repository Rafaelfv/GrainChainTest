package com.rafaelfv.grainchaintest.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.ui.fragments.FragmentMain
import com.rafaelfv.grainchaintest.utils.FRAGMENT_TAG_MAIN
import com.rafaelfv.grainchaintest.utils.setFragment
import com.rafaelfv.grainchaintest.viewmodels.MainActivityViewModel

import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this@MainActivity).get(MainActivityViewModel::class.java)
        this.supportFragmentManager.setFragment(
            viewModel.getFragmentMain(),
            R.id.container_main_activity,
            FRAGMENT_TAG_MAIN
        )

    }
}