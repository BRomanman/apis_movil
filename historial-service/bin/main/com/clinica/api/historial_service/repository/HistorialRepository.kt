package com.clinica.api.historial_service.repository

import com.clinica.api.historial_service.model.Historial
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistorialRepository : JpaRepository<Historial, Long> {
    fun findByUsuarioId(usuarioId: Long): List<Historial>
}
