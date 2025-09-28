package com.clinica.api.personal_service.model

import jakarta.persistence.*

@Entity
@Table(name = "especialidad")
data class Especialidad(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    var id: Long? = null,

    var nombre: String,

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    var doctor: Doctor
)