package com.clinica.api.citas.repository

import com.clinica.api.citas.model.Cita
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CitaRepository : JpaRepository<Cita, Long> {

    fun findByIdUsuario(idUsuario: Long): List<Cita>

    fun findByIdUsuarioAndFechaCitaAfter(idUsuario: Long, fecha: LocalDateTime): List<Cita>
}