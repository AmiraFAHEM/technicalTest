package com.example.technicaltest.data.repository

import com.example.technicaltest.data.localdb.dao.PhotosDao
import com.example.technicaltest.model.PhotosItem
import com.example.technicaltest.utils.NetworkBoundRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.example.technicaltest.utils.State
import com.premedit.runningcare.data.api.ApiService
import java.util.*

@ExperimentalCoroutinesApi
class PhotosRepository constructor(private val apiService : ApiService, private val photosDao: PhotosDao){

    fun getPhotosByAlbumId(userId : Int,albumId : Int) : Flow<State<List<PhotosItem>>> {
        return object : NetworkBoundRepository<List<PhotosItem>, List<PhotosItem>>() {
            override suspend fun saveNetworkResult(item: List<PhotosItem>) {
                photosDao.insert(item)
            }
            override fun shouldFetch(data: List<PhotosItem>?): Boolean {
                return data == null || data.isEmpty()
            }
            override fun loadFromDb(): Flow<List<PhotosItem>?> {
                return photosDao.getPhotosByAlbumId(albumId)
            }
            override suspend fun fetchFromNetwork(): Response<List<PhotosItem>> {
                return apiService.getPhotosByAlbumId(userId,albumId)
            }
        }.asFlow()
    }

}
