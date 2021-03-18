package com.example.technicaltest

import android.app.Application
import android.content.Context
import com.example.technicaltest.data.localdb.RoomLocalDb
import com.example.technicaltest.injection.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.internal.immutableListOf
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
@ExperimentalCoroutinesApi
@FlowPreview
class ApplicationCore : Application(){


    override fun onCreate() {
        super.onCreate()
        applicationCore = this
        roomDataBaseInstance = RoomLocalDb.getInstance(this)
        setUpDependencyInjection()
    }

    private fun setUpDependencyInjection() {
        startKoin {
            androidContext(this@ApplicationCore)
            modules(
                immutableListOf(
                    appModule,
                    viewModelModule,
                    repositoryModule,
                    daoModule
                )
            )
        }
    }

    companion object {
        private lateinit var applicationCore : ApplicationCore
        lateinit var roomDataBaseInstance: RoomLocalDb
        val context: Context
            get() = applicationCore.applicationContext
    }
}