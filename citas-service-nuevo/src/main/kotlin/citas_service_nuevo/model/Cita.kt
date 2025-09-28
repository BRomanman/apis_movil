package com.clinica.api.citas.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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

    // --- CAMBIO CLAVE AQUÍ ---
    // Ahora es una relación, no un simple ID.
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    // --- Y AQUÍ TAMBIÉN ---
    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    var doctor: Doctor,

    @Column(name = "id_consulta")
    var idConsulta: Long? = null
)