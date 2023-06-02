package com.example.practica5

import android.app.Application
import androidx.room.Room
import com.example.practica5.data.database.MonumentDataBase
import com.example.practica5.utils.MonumentsConstant.DATABASE_NAME

class MonumentsApp : Application() {
    private lateinit var monumentDataBase: MonumentDataBase
    override fun onCreate() {
        super.onCreate()
        monumentDataBase =  Room.databaseBuilder(this, MonumentDataBase::class.java, DATABASE_NAME).build()
    }

    fun getDataBase(): MonumentDataBase {
        return monumentDataBase
    }
}