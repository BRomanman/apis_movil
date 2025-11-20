package citas_service_nuevo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    // IMPORTANTE: Quitamos @GeneratedValue para mantener el ID original del médico
    @Column(name = "id_doctor")
    private Long id;

    @Column(name = "tarifa_consulta")
    private Integer tarifaConsulta;

    @Column
    private Long sueldo;

    @Column
    private Long bono;

    @Column
    private Boolean activo = true;

    // CascadeType.ALL permite que si la cita trae un doctor nuevo, se guarde automático
    @OneToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "id_usuario") // Sin nullable = false
    private Usuario usuario;

    public Doctor() {
    }
    
    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTarifaConsulta() {
        return tarifaConsulta;
    }

    public void setTarifaConsulta(Integer tarifaConsulta) {
        this.tarifaConsulta = tarifaConsulta;
    }

    public Long getSueldo() {
        return sueldo;
    }

    public void setSueldo(Long sueldo) {
        this.sueldo = sueldo;
    }

    public Long getBono() {
        return bono;
    }

    public void setBono(Long bono) {
        this.bono = bono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}