package com.example.practica5.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.practica5.data.database.dao.MonumentDAO
import com.example.practica5.data.database.entities.MonumentDBO
import com.example.practica5.utils.ImageDBOConverter
import com.example.practica5.utils.LocationDBOConverter

@Database(entities = [MonumentDBO::class], version = 1)
@TypeConverters(ImageDBOConverter::class, LocationDBOConverter::class)
abstract class MonumentDataBase: RoomDatabase() {
    abstract fun getMonumentDAO():MonumentDAO
}