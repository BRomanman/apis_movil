package com.clinica.api.usuario_service.repository

import com.clinica.api.usuario_service.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByCorreo(correo: String): Optional<Usuario>
    fun existsByCorreo(correo: String): Boolean
}
