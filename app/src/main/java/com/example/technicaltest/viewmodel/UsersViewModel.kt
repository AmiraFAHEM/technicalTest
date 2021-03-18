package com.example.technicaltest.viewmodel

import androidx.lifecycle.*
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.data.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import com.example.technicaltest.utils.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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
}
