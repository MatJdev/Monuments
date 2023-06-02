package com.example.practica5.utils

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.practica5.data.database.entities.ImageDBO
import com.example.practica5.data.database.entities.LocationDBO
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ImageDBOConverter {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, ImageDBO::class.java)
    private val adapter: JsonAdapter<List<ImageDBO>> = moshi.adapter(listType)

    @TypeConverter
    fun fromJson(json: String): List<ImageDBO> {
        return adapter.fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun toJson(images: List<ImageDBO>): String {
        return adapter.toJson(images)
    }
}

class LocationDBOConverter {
    private val moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<LocationDBO> = moshi.adapter(LocationDBO::class.java)

    @TypeConverter
    fun fromJson(json: String): LocationDBO? {
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun toJson(location: LocationDBO): String {
        return adapter.toJson(location)
    }
}