package com.clinica.api.usuario_service.dto

import java.math.BigDecimal

data class UsuarioUpdateRequest(
    val telefono: String?,
    val contrasena: String?,
    val sueldo: BigDecimal?,
    val tarifaConsulta: BigDecimal?
)
