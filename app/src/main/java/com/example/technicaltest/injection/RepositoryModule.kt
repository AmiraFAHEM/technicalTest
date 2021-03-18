package com.example.technicaltest.injection

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module
import com.example.technicaltest.data.repository.*

@FlowPreview
@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { UsersRepository(get(),get()) }
    single { AlbumsRepository(get(),get()) }
    single { PhotosRepository(get(),get()) }

}