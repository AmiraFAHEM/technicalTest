package com.example.technicaltest.injection

import com.example.technicaltest.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { UsersViewModel(get()) }
    viewModel { AlbumsViewModel(get()) }
    viewModel { PhotosViewModel(get()) }
}