package com.example.technicaltest.viewmodel

import androidx.lifecycle.*
import com.example.technicaltest.model.PhotosItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import com.example.technicaltest.utils.State
import com.example.technicaltest.data.repository.PhotosRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PhotosViewModel constructor(private val photosRepository: PhotosRepository) : ViewModel() {

    private val _albumPhotosListLiveData = MutableLiveData<State<List<PhotosItem>>>()
    val albumPhotosListLiveData: LiveData<State<List<PhotosItem>>>
        get() = _albumPhotosListLiveData

    fun getPhotosByAlbumId(albumId : Int,userId : Int) {
        viewModelScope.launch {
            photosRepository.getPhotosByAlbumId(userId,albumId).collect {
                _albumPhotosListLiveData.value = it
                if (it is State.Success) {
                    this.cancel()
                }
            }
        }
    }
}
