package com.example.technicaltest.data.localdb.dao

import androidx.room.*
import com.example.technicaltest.data.localdb.dao.BaseDao
import com.example.technicaltest.model.UserItem
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UsersDao : BaseDao<UserItem> {

    @Query("SELECT * FROM UserItem")
    fun getAllUsers(): Flow<List<UserItem>>

    @Query("SELECT * FROM UserItem WHERE id = :id")
    fun getUserById(id: Int): Flow<UserItem>

    @Query("DELETE FROM UserItem")
    fun clear()
}