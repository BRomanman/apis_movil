package com.clinica.api.seguros_service.dto;

import jakarta.validation.constraints.Size;

public class SeguroCancelRequest {

    @Size(max = 200)
    private String motivo;

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
