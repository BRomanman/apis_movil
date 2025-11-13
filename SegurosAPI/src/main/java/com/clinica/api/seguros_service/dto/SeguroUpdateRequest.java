package com.clinica.api.seguros_service.dto;

import jakarta.validation.constraints.Size;

public class SeguroUpdateRequest {

    @Size(max = 100)
    private String nombreSeguro;

    @Size(max = 200)
    private String descripcion;

    private Long usuarioId;

    public String getNombreSeguro() {
        return nombreSeguro;
    }

    public void setNombreSeguro(String nombreSeguro) {
        this.nombreSeguro = nombreSeguro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
