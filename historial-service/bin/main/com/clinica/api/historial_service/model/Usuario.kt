package com.clinica.api.historial_service.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    var id: Long? = null,

    var nombre: String,
    var apellido: String,

    @Column(name = "fecha_nacimiento")
    var fechaNacimiento: LocalDate,

    var correo: String,
    var telefono: String?,
    var contrasena: String,

    @Column(name = "id_rol")
    var rolId: Long
)
