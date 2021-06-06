package com.rafaelfv.grainchaintest.di.component

import com.rafaelfv.grainchaintest.di.modules.ContextModule
import com.rafaelfv.grainchaintest.viewmodels.FragmentMainViewModel
import dagger.Component

@Component(modules = [ContextModule::class])
interface ComponentInjector {

    /**
     * Injectors for viewModels
     */
    fun inject(fragmentMainViewModel: FragmentMainViewModel)

    @Component.Builder
    interface Builder{
        fun build(): ComponentInjector
        fun contextModule(context: ContextModule): Builder
    }
}