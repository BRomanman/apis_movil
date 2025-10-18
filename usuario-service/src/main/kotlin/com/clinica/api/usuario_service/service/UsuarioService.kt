package com.clinica.api.usuario_service.service

import com.clinica.api.usuario_service.dto.LoginRequest
import com.clinica.api.usuario_service.dto.LoginResponse
import com.clinica.api.usuario_service.dto.UsuarioRegistrationRequest
import com.clinica.api.usuario_service.dto.UsuarioResponse
import com.clinica.api.usuario_service.dto.UsuarioUpdateRequest
import com.clinica.api.usuario_service.model.Doctor
import com.clinica.api.usuario_service.model.Rol
import com.clinica.api.usuario_service.model.Usuario
import com.clinica.api.usuario_service.repository.DoctorRepository
import com.clinica.api.usuario_service.repository.RolRepository
import com.clinica.api.usuario_service.repository.UsuarioRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
@Transactional
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val doctorRepository: DoctorRepository,
    private val rolRepository: RolRepository
) {

    fun registrarUsuario(request: UsuarioRegistrationRequest): UsuarioResponse {
        if (usuarioRepository.existsByCorreo(request.correo)) {
            throw DataIntegrityViolationException("El correo '${request.correo}' ya se encuentra registrado")
        }

        val rol = rolRepository.findById(request.rolId)
            .orElseThrow { EntityNotFoundException("Rol con id ${request.rolId} no encontrado") }

        if (!isDoctor(rol) && (request.tarifaConsulta != null || request.sueldo != null || request.bono != null)) {
            throw IllegalArgumentException("Solo los usuarios con rol Doctor pueden especificar tarifa, sueldo o bono")
        }

        if (isDoctor(rol) && request.tarifaConsulta == null) {
            throw IllegalArgumentException("Para el rol Doctor se requiere 'tarifaConsulta'")
        }

        val usuario = usuarioRepository.save(
            Usuario(
                nombre = request.nombre,
                apellido = request.apellido,
                fechaNacimiento = request.fechaNacimiento,
                correo = request.correo,
                telefono = request.telefono,
                contrasena = request.contrasena,
                rol = rol
            )
        )

        val doctor = maybeCreateOrUpdateDoctor(usuario, request.tarifaConsulta, request.sueldo, request.bono)

        return toResponse(usuario, doctor)
    }

    fun listarUsuarios(): List<UsuarioResponse> {
        return usuarioRepository.findAll()
            .map { usuario -> toResponse(usuario, doctorRepository.findByUsuario(usuario).orElse(null)) }
    }

    fun obtenerUsuario(id: Long): UsuarioResponse {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Usuario con id $id no encontrado") }
        val doctor = doctorRepository.findByUsuario(usuario).orElse(null)
        return toResponse(usuario, doctor)
    }

    fun actualizarUsuario(id: Long, request: UsuarioUpdateRequest): UsuarioResponse {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Usuario con id $id no encontrado") }

        request.telefono?.let { usuario.telefono = it }
        request.contrasena?.let { usuario.contrasena = it }

        val doctor = updateDoctorData(usuario, request.sueldo, request.tarifaConsulta)

        val usuarioActualizado = usuarioRepository.save(usuario)
        return toResponse(usuarioActualizado, doctor)
    }

    fun eliminarUsuario(id: Long) {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Usuario con id $id no encontrado") }

        if (doctorRepository.existsByUsuario(usuario)) {
            doctorRepository.deleteByUsuario(usuario)
        }

        usuarioRepository.delete(usuario)
    }

    fun login(request: LoginRequest): LoginResponse {
        val usuario = usuarioRepository.findByCorreo(request.correo).orElse(null)
        return if (usuario != null && usuario.contrasena == request.contrasena) {
            val doctor = doctorRepository.findByUsuario(usuario).orElse(null)
            LoginResponse(
                authenticated = true,
                message = "Autenticación exitosa",
                usuario = toResponse(usuario, doctor)
            )
        } else {
            LoginResponse(
                authenticated = false,
                message = "Correo o contraseña inválidos"
            )
        }
    }

    private fun updateDoctorData(
        usuario: Usuario,
        sueldo: BigDecimal?,
        tarifaConsulta: BigDecimal?
    ): Doctor? {
        if (!isDoctor(usuario)) {
            if (sueldo != null || tarifaConsulta != null) {
                throw IllegalArgumentException("El usuario no posee rol de doctor, no se pueden actualizar campos de doctor")
            }
            return null
        }

        val doctor = doctorRepository.findByUsuario(usuario).orElse(null)

        if (doctor == null) {
            return maybeCreateOrUpdateDoctor(usuario, tarifaConsulta, sueldo, null)
        }

        sueldo?.let { doctor.sueldo = it }
        tarifaConsulta?.let { doctor.tarifaConsulta = it }

        return doctorRepository.save(doctor)
    }

    private fun maybeCreateOrUpdateDoctor(
        usuario: Usuario,
        tarifaConsulta: BigDecimal?,
        sueldo: BigDecimal?,
        bono: BigDecimal?
    ): Doctor? {
        if (tarifaConsulta == null && sueldo == null && bono == null) {
            return doctorRepository.findByUsuario(usuario).orElse(null)
        }

        if (!isDoctor(usuario)) {
            throw IllegalArgumentException("El usuario no posee rol de doctor, no se puede crear información de doctor")
        }

        var doctor = doctorRepository.findByUsuario(usuario).orElse(null)

        if (doctor == null) {
            if (tarifaConsulta == null) {
                throw IllegalArgumentException("Se requiere 'tarifaConsulta' para crear el registro de doctor")
            }
            doctor = Doctor(
                usuario = usuario,
                tarifaConsulta = tarifaConsulta,
                sueldo = sueldo,
                bono = bono ?: BigDecimal.ZERO
            )
        } else {
            tarifaConsulta?.let { doctor.tarifaConsulta = it }
            sueldo?.let { doctor.sueldo = it }
            bono?.let { doctor.bono = it }
        }

        return doctorRepository.save(doctor)
    }

    private fun toResponse(usuario: Usuario, doctor: Doctor?): UsuarioResponse {
        return UsuarioResponse(
            id = usuario.id ?: 0L,
            nombre = usuario.nombre,
            apellido = usuario.apellido,
            fechaNacimiento = usuario.fechaNacimiento,
            correo = usuario.correo,
            telefono = usuario.telefono,
            rolId = usuario.rol.id ?: 0L,
            rolNombre = usuario.rol.nombre,
            sueldo = doctor?.sueldo,
            tarifaConsulta = doctor?.tarifaConsulta
        )
    }

    private fun isDoctor(usuario: Usuario): Boolean {
        return isDoctor(usuario.rol)
    }

    private fun isDoctor(rol: Rol): Boolean {
        return rol.nombre.equals("doctor", ignoreCase = true)
    }
}
