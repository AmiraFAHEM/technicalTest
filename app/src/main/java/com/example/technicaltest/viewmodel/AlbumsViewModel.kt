package com.example.technicaltest.viewmodel

import androidx.lifecycle.*
import com.example.technicaltest.model.AlbumsItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import com.example.technicaltest.utils.State
import com.example.technicaltest.data.repository.AlbumsRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AlbumsViewModel constructor(private val albumsRepository: AlbumsRepository) : ViewModel() {

    private val _userAlbumsListLiveData = MutableLiveData<State<List<AlbumsItem>>>()
    val userAlbumsListLiveData: LiveData<State<List<AlbumsItem>>>
        get() = _userAlbumsListLiveData

    fun getUserAlbums(userId : Int) {
        viewModelScope.launch {
            albumsRepository.getUserAlbums(userId).collect {
                _userAlbumsListLiveData.value = it
                if (it is State.Success) {
                    this.cancel()
                }
            }
        }
    }
}
