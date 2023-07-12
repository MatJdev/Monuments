package com.example.practica5.mymonuments.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.model.bo.monument.MonumentBO
import javax.inject.Inject

class GetMyMonumentsUseCase @Inject constructor(private val monumentRepository: MonumentRepository) {
    suspend operator fun invoke(): List<MonumentBO>? = monumentRepository.getMyMonuments()
}