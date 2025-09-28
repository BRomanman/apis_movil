package com.clinica.api.personal_service.model

import jakarta.persistence.*

@Entity
@Table(name = "doctor")
data class Doctor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    var id: Long? = null,

    var sueldo: Long?,

    @OneToOne
    @JoinColumn(name = "id_usuario")
    var usuario: Usuario
)