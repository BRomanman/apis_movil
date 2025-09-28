package com.clinica.api.citas.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    var id: Long? = null,

    @Column(nullable = false)
    var nombre: String,

    @Column(nullable = false)
    var apellido: String,

    @Column(name = "fecha_nacimiento", nullable = false)
    var fechaNacimiento: LocalDateTime,

    @Column(nullable = false, unique = true)
    var correo: String,

    var telefono: String?,

    @Column(nullable = false)
    var contrasena: String,

    @Column(name = "id_rol", nullable = false)
    var idRol: Long
)