package com.clinica.api.citas.service

import com.clinica.api.citas.model.Cita
import com.clinica.api.citas.repository.CitaRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class CitaService(private val citaRepository: CitaRepository) {

    fun findAll(): List<Cita> {
        return citaRepository.findAll()
    }

    fun findById(id: Long): Cita {
        return citaRepository.findById(id).get()
    }

    fun save(cita: Cita): Cita {
        if (cita.id == null) {
            cita.estado = "CONFIRMADA"
        }
        return citaRepository.save(cita)
    }

    fun deleteById(id: Long) {
        citaRepository.deleteById(id)
    }

    fun findByUsuario(idUsuario: Long): List<Cita> {
        return citaRepository.findByIdUsuario(idUsuario)
    }
}