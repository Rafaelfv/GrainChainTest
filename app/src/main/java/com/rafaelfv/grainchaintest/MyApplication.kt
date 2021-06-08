package com.rafaelfv.grainchaintest

import android.app.Application
import com.rafaelfv.grainchaintest.db.AppDataBase
import com.rafaelfv.grainchaintest.di.component.ComponentInjector
import com.rafaelfv.grainchaintest.di.component.DaggerComponentInjector
import com.rafaelfv.grainchaintest.di.modules.ContextModule
import com.rafaelfv.grainchaintest.di.modules.DataBaseModule

class MyApplication: Application() {

    companion object {
        lateinit var component: ComponentInjector
        private lateinit var db: AppDataBase
    }

    override fun onCreate() {
        super.onCreate()
        val contextModule = ContextModule()
        contextModule.AppModule(this.applicationContext)
        db = AppDataBase.invoke(applicationContext)
        val dbModule = DataBaseModule()
        dbModule.AppDataBaseModule(db)
        //db = AppDataBase.invoke(applicationContext)

        component = DaggerComponentInjector.builder().contextModule(contextModule).dataBaseModule(dbModule).build()
    }
}