package com.example.practica5.local.monuments.dbo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDBO(
    val id: Long = 0,
    val url: String= ""
)
