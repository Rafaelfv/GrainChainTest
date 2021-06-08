package com.rafaelfv.grainchaintest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafaelfv.grainchaintest.data.Dot
import com.rafaelfv.grainchaintest.data.RouteInfo
import com.rafaelfv.grainchaintest.utils.DataBaseName

@Database(entities = [Dot::class, RouteInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun routeDao(): RouteDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDataBase::class.java, DataBaseName
        ).allowMainThreadQueries().build()
    }
}