package com.example.technicaltest.data.repository

import com.example.technicaltest.data.localdb.dao.AlbumsDao
import com.example.technicaltest.model.AlbumsItem
import com.example.technicaltest.utils.NetworkBoundRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.example.technicaltest.utils.State
import com.premedit.runningcare.data.api.ApiService
import java.util.*

@ExperimentalCoroutinesApi
class AlbumsRepository constructor(private val apiService : ApiService, private val albumsDao: AlbumsDao){

    fun getUserAlbums(userId : Int) : Flow<State<List<AlbumsItem>>> {
        return object : NetworkBoundRepository<List<AlbumsItem>, List<AlbumsItem>>() {
            override suspend fun saveNetworkResult(item: List<AlbumsItem>) {
                albumsDao.insert(item)
            }
            override fun shouldFetch(data: List<AlbumsItem>?): Boolean {
                return data == null || data.isEmpty()
            }
            override fun loadFromDb(): Flow<List<AlbumsItem>?> {
                return albumsDao.getAllAlbums()
            }
            override suspend fun fetchFromNetwork(): Response<List<AlbumsItem>> {
                return apiService.getUserAlbums(userId)
            }
        }.asFlow()
    }
}
