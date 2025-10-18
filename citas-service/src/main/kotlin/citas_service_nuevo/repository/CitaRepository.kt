package citas_service_nuevo.repository

import citas_service_nuevo.model.Cita
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CitaRepository : JpaRepository<Cita, Long> {

    fun findByUsuarioId(idUsuario: Long): List<Cita>

    // El metodo se llama findByUsuarioIdAndFechaCitaAfter
    fun findByUsuarioIdAndFechaCitaAfter(idUsuario: Long, fecha: LocalDateTime): List<Cita>
}