package com.example.technicaltest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserItem(
    @PrimaryKey
    val id: Int,
    val name: String,
    val username: String,
    val website: String,
    val email: String,
    val phone: String
)
