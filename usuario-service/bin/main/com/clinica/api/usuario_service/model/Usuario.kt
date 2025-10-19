package com.clinica.api.usuario_service.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    var id: Long? = null,

    @Column(nullable = false, length = 60)
    var nombre: String,

    @Column(nullable = false, length = 100)
    var apellido: String,

    @Column(name = "fecha_nacimiento", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var fechaNacimiento: LocalDate,

    @Column(nullable = false, unique = true, length = 100)
    var correo: String,

    @Column(length = 12)
    var telefono: String?,

    @Column(nullable = false, length = 100)
    var contrasena: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    var rol: Rol
)
