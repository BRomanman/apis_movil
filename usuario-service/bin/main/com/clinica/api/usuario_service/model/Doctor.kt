package com.clinica.api.usuario_service.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "doctor")
data class Doctor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    var id: Long? = null,

    @Column(name = "tarifa_consulta", nullable = false, precision = 10, scale = 2)
    var tarifaConsulta: BigDecimal,

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @Column(precision = 12, scale = 2)
    var sueldo: BigDecimal? = null,

    @Column(precision = 12, scale = 2)
    var bono: BigDecimal? = null
)
