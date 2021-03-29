package com.example.technicaltest.viewmodel

import androidx.annotation.MainThread
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

class UsersViewModel constructor(private val usersRepository: UsersRepository) : ViewModel() {


    private val trigger = MutableLiveData(Unit)

    val usersListLiveData: LiveData<State<List<UserItem>>> = trigger.switchMap {
        usersRepository.getUsersList().asLiveData()
    }

    //On pourra utiliser la methode retry si jamais on arrive pas à charger les données,
    // et qu'on laisse l'utilisateur les recharger lui même avec un bouton "retry" sur la page d'erreur par ex
    @MainThread
    fun retry() {
        trigger.value = Unit
    }



    //TODO improve later delay
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
