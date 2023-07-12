package com.example.practica5.commonfeatures.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.model.bo.location.LocationBO
import com.example.practica5.model.bo.monument.MonumentBO
import javax.inject.Inject

class InsertMonumentUseCase @Inject constructor(
    private val monumentRepository: MonumentRepository
) {
    private var monument: MonumentBO =
        MonumentBO(
            -1, "", "", "", emptyList(), LocationBO(0.0, 0.0),
            "", "", "", false, "", false
        )

    suspend operator fun invoke() {
        monumentRepository.insertOneMonument(monument)
    }

    fun setMonument(monument: MonumentBO) {
        this.monument = monument
    }
}