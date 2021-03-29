package com.example.technicaltest.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.example.technicaltest.data.repository.UsersRepository
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.utils.State

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
}
