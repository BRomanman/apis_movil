package com.clinica.api.usuario_service.controller

import com.clinica.api.usuario_service.dto.LoginRequest
import com.clinica.api.usuario_service.dto.LoginResponse
import com.clinica.api.usuario_service.dto.UsuarioRegistrationRequest
import com.clinica.api.usuario_service.dto.UsuarioResponse
import com.clinica.api.usuario_service.dto.UsuarioUpdateRequest
import com.clinica.api.usuario_service.service.UsuarioService
import jakarta.persistence.EntityNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/usuarios")
class UsuarioController(private val usuarioService: UsuarioService) {

    //crea un usuario nuevo; recibe UsuarioRegistrationRequest en el cuerpo
    @PostMapping
    fun registrarUsuario(@RequestBody request: UsuarioRegistrationRequest): ResponseEntity<UsuarioResponse> {
        return try {
            val response = usuarioService.registrarUsuario(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (ex: DataIntegrityViolationException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
    //obtener todos los usuarios
    @GetMapping
    fun listarUsuarios(): ResponseEntity<List<UsuarioResponse>> {
        val usuarios = usuarioService.listarUsuarios()
        return if (usuarios.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(usuarios)
        }
    }

    //obtener usuario por id
    @GetMapping("/{id}")
    fun obtenerUsuario(@PathVariable id: Long): ResponseEntity<UsuarioResponse> {
        return try {
            val usuario = usuarioService.obtenerUsuario(id)
            ResponseEntity.ok(usuario)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
    //actualiza los datos de un usuario
    @PutMapping("/{id}")
    fun actualizarUsuario(
        @PathVariable id: Long,
        @RequestBody request: UsuarioUpdateRequest
    ): ResponseEntity<UsuarioResponse> {
        return try {
            val usuario = usuarioService.actualizarUsuario(id, request)
            ResponseEntity.ok(usuario)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    //elimina lógicamente al usuario
    @DeleteMapping("/{id}")
    fun eliminarUsuario(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            usuarioService.eliminarUsuario(id)
            ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
    // autentica credenciales y devuelve el estado de login
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = usuarioService.login(request)
        val status = if (response.authenticated) HttpStatus.OK else HttpStatus.UNAUTHORIZED
        return ResponseEntity.status(status).body(response)
    }
}
