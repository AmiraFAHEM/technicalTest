package com.example.technicaltest.utils

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    fun asFlow() = flow<State<RESULT>> {
        emit(State.loading())

        val dbValue = loadFromDb().first()
        if (shouldFetch(dbValue)) {
            emit(State.loading())
            val apiResponse = fetchFromNetwork()
            if (apiResponse.isSuccessful) {
                processResponse(apiResponse)?.let { saveNetworkResult(it) }
                emitAll(loadFromDb().mapNotNull {
                    it?.let {
                        State.success<RESULT>(it)
                    }
                })
            } else {
                onFetchFailed()
                emit(State.error(apiResponse.message()))
            }
        } else {
            emitAll(loadFromDb().mapNotNull {
                it?.let {
                    State.success(it)
                }

            })
        }
    }.catch { e ->
        // Exception occurred! Emit error
        emit(State.error("Network error!"))
        e.printStackTrace()
    }

    protected open fun onFetchFailed() {
        // Implement in sub-classes to handle errors
    }

    @WorkerThread
    protected open fun processResponse(response: Response<REQUEST>?) = response?.body()

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(item: REQUEST)

    @MainThread
    protected abstract fun shouldFetch(data: RESULT?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<RESULT?>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<REQUEST>
}
