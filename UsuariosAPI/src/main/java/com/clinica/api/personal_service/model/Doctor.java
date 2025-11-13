package com.clinica.api.personal_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private Long id;

    @Column(name = "tarifa_consulta")
    private Integer tarifaConsulta;

    @Column
    private Long sueldo;

    @Column
    private Long bono;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public Doctor() {
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
