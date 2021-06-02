package com.rafaelfv.grainchaintest.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rafaelfv.grainchaintest.R

import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

    }
}