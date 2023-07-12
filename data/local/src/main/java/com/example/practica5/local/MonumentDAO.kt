package com.example.practica5.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practica5.local.monuments.dbo.MonumentDBO

@Dao
interface MonumentDAO {
    @Query("SELECT * FROM monument_table ORDER BY id ASC")
    suspend fun getAllMonuments(): List<MonumentDBO>

    @Query("SELECT * FROM monument_table WHERE country = :country")
    suspend fun getFilteredMonuments(country: String): List<MonumentDBO>

    @Query("SELECT * FROM monument_table ORDER BY :sortMode")
    suspend fun getSortedMonuments(sortMode: String): List<MonumentDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonument(monuments: List<MonumentDBO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneMonument(monument: MonumentDBO)

    @Delete
    suspend fun deleteMonument(monument: MonumentDBO)

    @Query("UPDATE monument_table SET isFavorite = :favorite WHERE id = :monumentId")
    suspend fun updateFavoriteMonument(monumentId: Long, favorite: Boolean)

    @Query("SELECT * FROM monument_table ORDER BY location_latitude DESC")
    suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentDBO>

    @Query("SELECT * FROM monument_table ORDER BY location_longitude ASC")
    suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentDBO>

    @Query("SELECT DISTINCT country FROM monument_table")
    suspend fun getUniqueCountries(): List<String>

    @Query("SELECT * FROM monument_table WHERE isFromMyMonuments = 1")
    suspend fun getMyMonuments(): List<MonumentDBO>

    @Query("SELECT * FROM monument_table WHERE isFavorite = 1")
    suspend fun getFavoriteMonuments(): List<MonumentDBO>
}