package com.clinica.api.usuario_service.dto

data class LoginResponse(
    val authenticated: Boolean,
    val message: String,
    val usuario: UsuarioResponse? = null
)
