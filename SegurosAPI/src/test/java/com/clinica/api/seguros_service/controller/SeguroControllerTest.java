package com.clinica.api.seguros_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.seguros_service.model.Seguro;
import com.clinica.api.seguros_service.model.SeguroEstado;
import com.clinica.api.seguros_service.service.SeguroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SeguroController.class)
class SeguroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeguroService seguroService;

    @Test
    @DisplayName("GET /api/v1/seguros responde 200 con resultados")
    void listarSeguros_returnsOk() throws Exception {
        when(seguroService.findAll()).thenReturn(List.of(seguro()));

        mockMvc.perform(get("/api/v1/seguros"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreSeguro").value("Dental"));
    }

    @Test
    @DisplayName("GET /api/v1/seguros responde 204 cuando no hay datos")
    void listarSeguros_returnsNoContent() throws Exception {
        when(seguroService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/seguros"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/seguros/{id} responde 404 cuando no existe")
    void obtenerSeguro_returnsNotFound() throws Exception {
        when(seguroService.findById(9L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(get("/api/v1/seguros/{id}", 9L))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/seguros responde 201 con el seguro creado")
    void crearSeguro_returnsCreated() throws Exception {
        when(seguroService.create(any(Seguro.class))).thenReturn(seguro());

        mockMvc.perform(post("/api/v1/seguros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguro())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.usuarioId").value(1L));
    }

    @Test
    @DisplayName("PUT /api/v1/seguros/{id} responde 404 cuando no existe")
    void actualizarSeguro_returnsNotFound() throws Exception {
        when(seguroService.update(any(Long.class), any(Seguro.class)))
            .thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(put("/api/v1/seguros/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguro())))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/v1/seguros/{id}/cancelacion responde 200")
    void cancelarSeguro_returnsOk() throws Exception {
        when(seguroService.cancel(any(Long.class), any(String.class))).thenReturn(seguro());

        mockMvc.perform(patch("/api/v1/seguros/{id}/cancelacion", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("motivo", "Fin de contrato"))))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v1/seguros/{id} responde 204")
    void eliminarSeguro_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/seguros/{id}", 4L))
            .andExpect(status().isNoContent());
    }

    private Seguro seguro() {
        Seguro seguro = new Seguro();
        seguro.setId(1L);
        seguro.setNombreSeguro("Dental");
        seguro.setDescripcion("Cobertura dental");
        seguro.setEstado(SeguroEstado.ACTIVO);
        seguro.setUsuarioId(1L);
        seguro.setFechaCancelacion(null);
        return seguro;
    }
}
