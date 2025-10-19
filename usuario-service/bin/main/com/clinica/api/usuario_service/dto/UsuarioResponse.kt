package com.clinica.api.usuario_service.dto

import java.math.BigDecimal
import java.time.LocalDate

data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: LocalDate,
    val correo: String,
    val telefono: String?,
    val rolId: Long,
    val rolNombre: String,
    val sueldo: BigDecimal?,
    val tarifaConsulta: BigDecimal?
)
