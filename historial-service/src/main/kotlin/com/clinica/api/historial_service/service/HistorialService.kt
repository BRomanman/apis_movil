package com.clinica.api.historial_service.service

import com.clinica.api.historial_service.model.Historial
import com.clinica.api.historial_service.repository.HistorialRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class HistorialService(private val historialRepository: HistorialRepository) {

    fun findAllHistoriales(): List<Historial> {
        return historialRepository.findAll()
    }

    fun findHistorialById(id: Long): Historial {
        return historialRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Historial no encontrado") }
    }
}
