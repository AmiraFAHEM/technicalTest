package com.example.technicaltest.data.localdb.dao

import androidx.room.*
import com.example.technicaltest.model.AlbumsItem
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface AlbumsDao : BaseDao<AlbumsItem> {

    @Query("SELECT * FROM AlbumsItem")
    fun getAllAlbums(): Flow<List<AlbumsItem>>

    @Query("SELECT * FROM AlbumsItem WHERE id = :id")
    fun getAlbumById(id: Int): Flow<AlbumsItem>

    @Query("DELETE FROM AlbumsItem")
    fun clear()
}