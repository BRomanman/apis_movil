package com.clinica.api.usuario_service.dto

data class LoginRequest(
    val correo: String,
    val contrasena: String
)
