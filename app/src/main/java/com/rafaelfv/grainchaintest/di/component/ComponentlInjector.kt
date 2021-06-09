package com.rafaelfv.grainchaintest.di.component

import com.rafaelfv.grainchaintest.di.modules.ContextModule
import com.rafaelfv.grainchaintest.di.modules.DataBaseModule
import com.rafaelfv.grainchaintest.viewmodels.FragmentMainViewModel
import com.rafaelfv.grainchaintest.viewmodels.FragmentRouteDetailViewModel
import com.rafaelfv.grainchaintest.viewmodels.FragmentRoutesViewModel
import dagger.Component

@Component(modules = [ContextModule::class, DataBaseModule::class])
interface ComponentInjector {

    /**
     * Injectors for viewModels
     */
    fun inject(fragmentMainViewModel: FragmentMainViewModel)
    fun inject(fragmentRoutesViewModel: FragmentRoutesViewModel)
    fun inject(fragmentRouteDetailViewModel: FragmentRouteDetailViewModel)

    @Component.Builder
    interface Builder{
        fun build(): ComponentInjector
        fun contextModule(context: ContextModule): Builder
        fun dataBaseModule(databaseModule: DataBaseModule): Builder
    }
}