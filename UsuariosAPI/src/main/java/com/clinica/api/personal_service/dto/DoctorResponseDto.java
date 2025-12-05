package com.clinica.api.personal_service.dto;

public class DoctorResponseDto {

    private Long idTrabajador;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Boolean activo;
    private String especialidad;
    private Integer tarifaConsulta;
    private Long sueldo;
    private Long bono;

    public Long getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Long idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
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
}
