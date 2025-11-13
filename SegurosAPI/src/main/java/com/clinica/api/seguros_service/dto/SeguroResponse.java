package com.clinica.api.seguros_service.dto;

import java.time.LocalDateTime;

public class SeguroResponse {

    private Long id;
    private String nombreSeguro;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCancelacion;
    private Long usuarioId;
    private String usuarioNombre;

    public SeguroResponse(Long id, String nombreSeguro, String descripcion, String estado,
                          LocalDateTime fechaCreacion, LocalDateTime fechaCancelacion,
                          Long usuarioId, String usuarioNombre) {
        this.id = id;
        this.nombreSeguro = nombreSeguro;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaCancelacion = fechaCancelacion;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombreSeguro() {
        return nombreSeguro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }
}
