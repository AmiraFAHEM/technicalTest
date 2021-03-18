package com.example.technicaltest.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AlbumsItem(
    @PrimaryKey
    val id: Int,
    val title: String,
    val userId: Int
)