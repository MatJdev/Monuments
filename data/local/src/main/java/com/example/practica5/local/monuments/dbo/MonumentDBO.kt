package com.example.practica5.local.monuments.dbo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.practica5.local.converter.ImageDBOConverter
import com.example.practica5.local.converter.LocationDBOConverter
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "monument_table")
@TypeConverters(ImageDBOConverter::class, LocationDBOConverter::class)
data class MonumentDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "city") val city: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "images") val images: List<ImageDBO> = emptyList(),
    @Embedded(prefix = "location_")
    val location: LocationDBO = LocationDBO(),
    @ColumnInfo(name = "urlExtraInformation") val urlExtraInformation: String = "",
    @ColumnInfo(name = "country") val country: String = "",
    @ColumnInfo(name = "countryCode") val countryCode: String = "",
    @ColumnInfo(name = "isFromMyMonuments") val isFromMyMonuments: Boolean = false,
    @ColumnInfo(name = "countryFlag") val countryFlag: String = "",
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean = false
)
