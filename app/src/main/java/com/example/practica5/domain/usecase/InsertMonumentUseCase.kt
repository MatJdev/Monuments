package com.example.practica5.domain.usecase

import com.example.practica5.domain.model.bo.LocationBO
import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.MonumentRepository
import javax.inject.Inject

class InsertMonumentUseCase @Inject constructor(
    private val monumentRepository: MonumentRepository
) {
    private var monument: MonumentBO =
        MonumentBO(-1, "", "", "", emptyList(), LocationBO(0.0, 0.0),
            "", "", "", false, "", false)

    suspend operator fun invoke() {
        monumentRepository.insertOneMonument(monument)
    }

    fun setMonument(monument: MonumentBO) {
        this.monument = monument
    }
}