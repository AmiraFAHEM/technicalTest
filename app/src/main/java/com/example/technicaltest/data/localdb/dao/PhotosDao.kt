package com.example.technicaltest.data.localdb.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.technicaltest.model.PhotosItem
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface PhotosDao : BaseDao<PhotosItem> {

    @Query("SELECT * FROM PhotosItem")
    fun getAllPhotos(): Flow<List<PhotosItem>>

    @Query("SELECT * FROM PhotosItem WHERE albumId = :albumId")
    fun getPhotosByAlbumId(albumId: Int): Flow<List<PhotosItem>>

    @Query("DELETE FROM PhotosItem")
    fun clear()


}