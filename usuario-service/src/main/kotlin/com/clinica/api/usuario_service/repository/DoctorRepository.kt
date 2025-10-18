package com.clinica.api.usuario_service.repository

import com.clinica.api.usuario_service.model.Doctor
import com.clinica.api.usuario_service.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DoctorRepository : JpaRepository<Doctor, Long> {
    fun findByUsuario(usuario: Usuario): Optional<Doctor>
    fun deleteByUsuario(usuario: Usuario)
    fun existsByUsuario(usuario: Usuario): Boolean
}
