package com.example.technicaltest.data.repository

import com.example.technicaltest.data.localdb.dao.UsersDao
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.utils.NetworkBoundRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.example.technicaltest.utils.State
import com.premedit.runningcare.data.api.ApiService
import java.util.*

@ExperimentalCoroutinesApi
class UsersRepository constructor(private val apiService : ApiService, private val usersDao: UsersDao){

    fun getUsersList() : Flow<State<List<UserItem>>> {
        return object : NetworkBoundRepository<List<UserItem>, List<UserItem>>() {
            override suspend fun saveNetworkResult(item: List<UserItem>) {
                usersDao.insert(item)
            }
            override fun shouldFetch(data: List<UserItem>?): Boolean {
                return data == null || data.isEmpty()
            }
            override fun loadFromDb(): Flow<List<UserItem>?> {
                return usersDao.getAllUsers()
            }
            override suspend fun fetchFromNetwork(): Response<List<UserItem>> {
                return apiService.getUsersList()
            }
        }.asFlow()
    }

}
