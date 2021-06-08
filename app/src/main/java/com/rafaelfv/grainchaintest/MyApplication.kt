package com.rafaelfv.grainchaintest

import android.app.Application
import androidx.room.Room
import com.rafaelfv.grainchaintest.db.AppDataBase
import com.rafaelfv.grainchaintest.di.component.ComponentInjector
import com.rafaelfv.grainchaintest.di.component.DaggerComponentInjector
import com.rafaelfv.grainchaintest.di.modules.ContextModule
import com.rafaelfv.grainchaintest.utils.DataBaseName

class MyApplication: Application() {

    companion object {
        lateinit var component: ComponentInjector
        private lateinit var db: AppDataBase
    }

    override fun onCreate() {
        super.onCreate()
        val contextModule = ContextModule()
        contextModule.AppModule(this.applicationContext)
        //db = AppDataBase.invoke(applicationContext)

        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, DataBaseName
        ).allowMainThreadQueries()
            .build()

        component = DaggerComponentInjector.builder().contextModule(
            contextModule
        ).build()
    }
}