package com.rafaelfv.grainchaintest.di.modules

import com.rafaelfv.grainchaintest.db.AppDataBase
import com.rafaelfv.grainchaintest.db.RouteDao
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    private lateinit var dataBase: AppDataBase

    fun AppDataBaseModule(dataBase: AppDataBase) {
        this.dataBase = dataBase
    }

    @Provides
    fun provideDatabase(): AppDataBase {
        return dataBase
    }

    @Provides
    fun provideRouteDao(): RouteDao {
        return dataBase.routeDao()
    }
}