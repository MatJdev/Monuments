package com.example.practica5.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.practica5.local.converter.ImageDBOConverter
import com.example.practica5.local.converter.LocationDBOConverter
import com.example.practica5.local.monuments.dbo.MonumentDBO

@Database(entities = [MonumentDBO::class], version = 1)
@TypeConverters(ImageDBOConverter::class, LocationDBOConverter::class)
abstract class MonumentDataBase: RoomDatabase() {
    abstract fun getMonumentDAO(): MonumentDAO
}