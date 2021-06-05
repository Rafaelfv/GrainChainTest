package com.rafaelfv.grainchaintest.di.component

import com.rafaelfv.grainchaintest.di.modules.ContextModule
import dagger.Component

@Component(modules = [ContextModule::class])
interface ComponentInjector {

    /**
     * Injectors for viewModels
     */
    //fun inject(viewModelListTop: ViewModelListTop)

    @Component.Builder
    interface Builder{
        fun build(): ComponentInjector
        fun contextModule(context: ContextModule): Builder
    }
}