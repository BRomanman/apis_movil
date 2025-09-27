package com.clinica.api.citas.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "cita")
data class Cita(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    var id: Long? = null,

    @Column(name = "fecha_cita", nullable = false)
    var fechaCita: LocalDateTime,

    @Column(name = "estado", nullable = false)
    var estado: String,

    @Column(name = "id_usuario", nullable = false)
    var idUsuario: Long,

    @Column(name = "id_doctor", nullable = false)
    var idDoctor: Long,

    @Column(name = "id_consulta")
    var idConsulta: Long? = null
)