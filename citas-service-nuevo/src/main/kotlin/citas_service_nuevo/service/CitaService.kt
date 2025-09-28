package citas_service_nuevo.service

import citas_service_nuevo.model.Cita
import citas_service_nuevo.repository.CitaRepository
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
        return citaRepository.findByUsuarioId(idUsuario)
    }

    //Metodo para la funcionalidad de recordatorios
    fun findProximasByUsuario(idUsuario: Long): List<Cita> {
        return citaRepository.findByUsuarioIdAndFechaCitaAfter(idUsuario, LocalDateTime.now())
    }
}