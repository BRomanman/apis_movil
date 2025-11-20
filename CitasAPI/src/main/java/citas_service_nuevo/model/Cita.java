package citas_service_nuevo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long id;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_doctor", nullable = false)
    private Long idDoctor;

    @Column(name = "pago")
    private Long pago;

    @Column(name = "id_receta")
    private Long idReceta;

    @Column(name = "id_resena")
    private Long idResena;

    @Column(name = "id_resumen")
    private Long idResumen;

    @Column(name = "id_consulta")
    private Long idConsulta;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = Boolean.TRUE;

    @Column(name = "observaciones_horario")
    private String observacionesHorario;

    public Cita() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Long getPago() {
        return pago;
    }

    public void setPago(Long pago) {
        this.pago = pago;
    }

    public Long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    public Long getIdResena() {
        return idResena;
    }

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public Long getIdResumen() {
        return idResumen;
    }

    public void setIdResumen(Long idResumen) {
        this.idResumen = idResumen;
    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getObservacionesHorario() {
        return observacionesHorario;
    }

    public void setObservacionesHorario(String observacionesHorario) {
        this.observacionesHorario = observacionesHorario;
    }
}
