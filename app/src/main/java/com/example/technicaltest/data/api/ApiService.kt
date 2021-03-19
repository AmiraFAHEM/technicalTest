package com.premedit.runningcare.data.api



import com.example.technicaltest.model.*
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface ApiService{
    @GET("/users")
    suspend fun getUsersList():  Response<List<UserItem>>

    @GET("/users/{userId}/albums")
    suspend fun getUserAlbums(@Path("userId") userId: Int?):  Response<List<AlbumsItem>>

    @GET("/users/{userId}/photos?")
    suspend fun getPhotosByAlbumId(@Path("userId") userId: Int?,@Query("albumId") albumId: Int):  Response<List<PhotosItem>>
}
