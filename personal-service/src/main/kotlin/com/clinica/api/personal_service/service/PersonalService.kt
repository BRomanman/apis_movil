package com.clinica.api.personal_service.service

import com.clinica.api.personal_service.model.Doctor
import com.clinica.api.personal_service.repository.DoctorRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class PersonalService(private val doctorRepository: DoctorRepository) {

    fun findAllDoctores(): List<Doctor> {
        return doctorRepository.findAll()
    }

    fun findDoctorById(id: Long): Doctor {
        return doctorRepository.findById(id).orElseThrow { Exception("Doctor no encontrado") }
    }

    fun saveDoctor(doctor: Doctor): Doctor {
        // Aquí se podrían añadir validaciones antes de guardar
        return doctorRepository.save(doctor)
    }

    fun deleteDoctorById(id: Long) {
        if (!doctorRepository.existsById(id)) {
            throw Exception("Doctor no encontrado para eliminar")
        }
        doctorRepository.deleteById(id)
    }
}