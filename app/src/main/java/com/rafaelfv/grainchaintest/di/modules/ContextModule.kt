package com.rafaelfv.grainchaintest.di.modules

import android.content.Context
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class ContextModule {

    private var context: Context? = null

    fun AppModule(@NonNull context: Context?) {
        this.context = context
    }

    @Singleton
    @Provides
    @NonNull
    fun provideContext(): Context? {
        return context
    }
}