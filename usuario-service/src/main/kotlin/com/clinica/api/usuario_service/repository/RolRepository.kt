package com.clinica.api.usuario_service.repository

import com.clinica.api.usuario_service.model.Rol
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RolRepository : JpaRepository<Rol, Long> {
    fun findByNombre(nombre: String): Optional<Rol>
}
