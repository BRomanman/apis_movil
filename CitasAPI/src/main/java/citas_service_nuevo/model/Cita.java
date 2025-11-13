package citas_service_nuevo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long id;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    @Column(name = "id_consulta")
    private Long idConsulta;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    @JsonProperty("idUsuario")
    public Long getIdUsuario() {
        return usuario != null ? usuario.getId() : null;
    }

    @JsonProperty("idUsuario")
    public void setIdUsuario(Long idUsuario) {
        if (idUsuario == null) {
            this.usuario = null;
            return;
        }
        if (this.usuario == null) {
            this.usuario = new Usuario();
        }
        this.usuario.setId(idUsuario);
    }

    @JsonProperty("idDoctor")
    public Long getIdDoctor() {
        return doctor != null ? doctor.getId() : null;
    }

    @JsonProperty("idDoctor")
    public void setIdDoctor(Long idDoctor) {
        if (idDoctor == null) {
            this.doctor = null;
            return;
        }
        if (this.doctor == null) {
            this.doctor = new Doctor();
        }
        this.doctor.setId(idDoctor);
    }
}
