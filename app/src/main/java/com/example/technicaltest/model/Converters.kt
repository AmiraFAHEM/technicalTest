package com.example.technicaltest.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class ConvertersPhotos {

    @TypeConverter
    fun stringToPhoto(json: String?): ArrayList<PhotosItem>? {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<PhotosItem?>?>() {}.getType()
        return gson.fromJson<ArrayList<PhotosItem>>(json, type)
    }

    @TypeConverter
    fun photoToString(list: ArrayList<PhotosItem?>?): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<PhotosItem?>?>() {}.getType()
        return gson.toJson(list, type)
    }
}