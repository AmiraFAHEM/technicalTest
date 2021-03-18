package com.example.technicaltest.injection

import com.example.technicaltest.ApplicationCore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module
@FlowPreview
@ExperimentalCoroutinesApi
val daoModule = module {
    single { ApplicationCore.roomDataBaseInstance.usersDao() }
    single { ApplicationCore.roomDataBaseInstance.albumsDao() }
    single { ApplicationCore.roomDataBaseInstance.photosDao() }

}
