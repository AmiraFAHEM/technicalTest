package com.example.technicaltest.injection


import androidx.core.content.ContextCompat
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.technicaltest.ApplicationCore
import com.example.technicaltest.R
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.premedit.runningcare.data.api.ApiService

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@FlowPreview
@ExperimentalCoroutinesApi
val appModule = module {
    single {
        CountingIdlingResource("TECHNICAL_TEST")
    }
    single { provideLogging() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofitService(get()) }


}
fun provideLogging() : HttpLoggingInterceptor{
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
    okHttpClient.apply {
        connectTimeout(10, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        addInterceptor(logging)

    }
    return okHttpClient.build()
}


@FlowPreview
@ExperimentalCoroutinesApi
fun provideRetrofitService(okHttpClient: OkHttpClient): ApiService {
    val apiInstance = Retrofit.Builder()
            .baseUrl(ApplicationCore.context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    return apiInstance
}


