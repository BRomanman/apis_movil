package com.clinica.api.personal_service.model

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    var id: Long? = null,

    var nombre: String,
    var apellido: String,
    var correo: String,
    // Agregaremos más campos según los necesitemos
)