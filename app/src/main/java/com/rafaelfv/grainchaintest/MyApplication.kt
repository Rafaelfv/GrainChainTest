package com.rafaelfv.grainchaintest

import android.app.Application
import com.rafaelfv.grainchaintest.di.component.ComponentInjector
import com.rafaelfv.grainchaintest.di.component.DaggerComponentInjector
import com.rafaelfv.grainchaintest.di.modules.ContextModule

class MyApplication: Application() {

    companion object {
        lateinit var component: ComponentInjector
    }

    override fun onCreate() {
        super.onCreate()
        val contextModule = ContextModule()
        contextModule.AppModule(this.applicationContext)
        component = DaggerComponentInjector.builder().contextModule(
            contextModule
        ).build()
    }
}