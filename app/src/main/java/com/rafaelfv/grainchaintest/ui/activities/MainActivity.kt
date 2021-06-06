package com.rafaelfv.grainchaintest.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.ui.fragments.FragmentMain
import com.rafaelfv.grainchaintest.utils.FRAGMENT_TAG_MAIN
import com.rafaelfv.grainchaintest.utils.REQUEST_CODE_LOCATION_TRIGGER
import com.rafaelfv.grainchaintest.utils.REQUEST_CODE_PERMISSION_LOCATION
import com.rafaelfv.grainchaintest.utils.setFragment
import com.rafaelfv.grainchaintest.viewmodels.MainActivityViewModel

import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            REQUEST_CODE_PERMISSION_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val fragmentMain = viewModel.getFragmentMain() as FragmentMain
                    fragmentMain.showCurrentPosition()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.location_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOCATION_TRIGGER -> {
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: Result Ok")
                    val fragmentMain = viewModel.getFragmentMain() as FragmentMain
                    fragmentMain.showCurrentPosition()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.location_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}