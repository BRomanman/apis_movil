package com.clinica.api.usuario_service.dto

import java.math.BigDecimal
import java.time.LocalDate

data class UsuarioRegistrationRequest(
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: LocalDate,
    val correo: String,
    val telefono: String?,
    val contrasena: String,
    val rolId: Long,
    val tarifaConsulta: BigDecimal? = null,
    val sueldo: BigDecimal? = null,
    val bono: BigDecimal? = null
)
