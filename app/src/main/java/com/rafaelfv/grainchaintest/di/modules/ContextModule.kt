package com.rafaelfv.grainchaintest.di.modules

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class ContextModule {

    private lateinit var context: Context

    fun AppModule(context: Context) {
        this.context = context
    }

    @Provides
    fun provideContext(): Context {
        return context
    }
}