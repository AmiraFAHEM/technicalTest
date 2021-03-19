package com.example.technicaltest.viewmodel

import androidx.lifecycle.*
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.data.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import com.example.technicaltest.utils.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.filterList

@ExperimentalCoroutinesApi
class UsersViewModel constructor(private val usersRepository: UsersRepository) : ViewModel() {

    private val _usersListLiveData = MutableLiveData<State<List<UserItem>>>()
    val usersListLiveData: LiveData<State<List<UserItem>>>
        get() = _usersListLiveData

    fun getUsersList() {
        viewModelScope.launch {
            usersRepository.getUsersList().collect {
                _usersListLiveData.value = it
                if (it is State.Success) {
                    this.cancel()
                }
            }
        }
    }
    fun getUsersSearchResult(query: String): Flow<State<List<UserItem>>> {
        return flow {
            delay(1000)
            usersRepository.getUsersList().collect {
                if (it is State.Success){
                    val filteredList = it.data.filter {
                        it.username.toUpperCase(Locale.getDefault()).contains(query.toUpperCase(Locale.getDefault()))
                    }
                    it.data = filteredList
                    emit(it)
                }
            }
        }
    }
}
