package com.clinica.api.citas.model

import jakarta.persistence.*

@Entity
@Table(name = "doctor")
data class Doctor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    var id: Long? = null,

    // Relacionamos al Doctor con un Usuario para obtener su nombre
    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @Column(name = "tarifa_consulta")
    var tarifaConsulta: Int
)