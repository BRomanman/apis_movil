package com.clinica.api.historial_service.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.historial_service.model.Historial;
import com.clinica.api.historial_service.service.HistorialService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HistorialController.class)
class HistorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialService historialService;

    @Test
    @DisplayName("GET /api/v1/historial/usuario/{usuarioId} responde 200 con contenido cuando existen registros")
    void getHistorialesByUsuario_returnsOk() throws Exception {
        Historial historial = new Historial();
        historial.setId(1L);
        historial.setIdUsuario(9L);
        historial.setEstado("Realizada");
        historial.setFechaCita(LocalDate.of(2024, 3, 1));

        when(historialService.findHistorialesByUsuarioId(9L)).thenReturn(List.of(historial));

        mockMvc.perform(get("/api/v1/historial/usuario/{usuarioId}", 9))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].estado").value("Realizada"))
            .andExpect(jsonPath("$[0].fechaCita").value("2024-03-01"))
            .andExpect(jsonPath("$[0].idUsuario").value(9));
    }

    @Test
    @DisplayName("GET /api/v1/historial/usuario/{usuarioId} responde 204 cuando no existen registros")
    void getHistorialesByUsuario_returnsNoContent() throws Exception {
        when(historialService.findHistorialesByUsuarioId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/historial/usuario/{usuarioId}", 7))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/historial/{id} responde 200 cuando el historial existe")
    void getHistorialById_returnsOk() throws Exception {
        Historial historial = new Historial();
        historial.setId(15L);
        historial.setEstado("Cancelada");
        historial.setIdUsuario(42L);
        historial.setFechaCita(LocalDate.of(2023, 12, 10));

        when(historialService.findHistorialById(15L)).thenReturn(historial);

        mockMvc.perform(get("/api/v1/historial/{id}", 15))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(15))
            .andExpect(jsonPath("$.estado").value("Cancelada"))
            .andExpect(jsonPath("$.fechaCita").value("2023-12-10"))
            .andExpect(jsonPath("$.idUsuario").value(42));
    }

    @Test
    @DisplayName("GET /api/v1/historial/{id} responde 404 cuando el historial no existe")
    void getHistorialById_returnsNotFound() throws Exception {
        when(historialService.findHistorialById(1L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(get("/api/v1/historial/{id}", 1))
            .andExpect(status().isNotFound());
    }
}
