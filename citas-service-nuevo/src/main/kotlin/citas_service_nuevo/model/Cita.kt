package citas_service_nuevo.model

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

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    var doctor: Doctor,

    @Column(name = "id_consulta")
    var idConsulta: Long? = null
)