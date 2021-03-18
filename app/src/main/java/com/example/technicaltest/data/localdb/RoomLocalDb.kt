/**
 * Copyright (C) 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.technicaltest.data.localdb


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.technicaltest.data.localdb.dao.AlbumsDao
import com.example.technicaltest.data.localdb.dao.PhotosDao
import com.example.technicaltest.data.localdb.dao.UsersDao
import com.example.technicaltest.model.*

@Database(
        entities = [
            AlbumsItem::class,
            PhotosItem::class,
            UserItem::class
        ],
        version = 1,
        exportSchema = false
)

abstract class RoomLocalDb : RoomDatabase() {
    abstract fun photosDao(): PhotosDao
    abstract fun usersDao() : UsersDao
    abstract fun albumsDao() : AlbumsDao

    companion object {
        @Volatile
        private var INSTANCE: RoomLocalDb? = null
        private val DATABASE_NAME = "room_db"

        fun getInstance(context: Context): RoomLocalDb =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context.applicationContext).also {
                        INSTANCE = it
                    }
                }

        private fun buildDatabase(appContext: Context): RoomLocalDb {
            return Room.databaseBuilder(appContext, RoomLocalDb::class.java, DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // Data is cache, so it is OK to delete
                    .build()
        }
    }
}
