package com.example.practica5.domain.usecase

import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.MonumentRepository

class DeleteMonumentUseCase(private val monumentRepository: MonumentRepository, private val monument: MonumentBO) {
    suspend operator fun invoke() {
        monumentRepository.deleteMonument(monument)
    }
}