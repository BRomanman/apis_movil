package com.clinica.api.personal_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.personal_service.controller.EspecialidadController.EspecialidadRequest;
import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Especialidad;
import com.clinica.api.personal_service.service.EspecialidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EspecialidadController.class)
class EspecialidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EspecialidadService especialidadService;

    @Test
    @DisplayName("GET /api/v1/doctores/{id}/especialidades responde 200 con la lista de especialidades")
    void getEspecialidadesByDoctor_returnsOk() throws Exception {
        Especialidad especialidad = new Especialidad();
        especialidad.setId(1L);
        especialidad.setNombre("Cardiología");
        Doctor doctor = new Doctor();
        doctor.setId(3L);
        especialidad.setDoctor(doctor);

        when(especialidadService.findByDoctorId(3L)).thenReturn(List.of(especialidad));

        mockMvc.perform(get("/api/v1/doctores/{id}/especialidades", 3L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Cardiología"))
            .andExpect(jsonPath("$[0].doctorId").value(3));
    }

    @Test
    @DisplayName("POST /api/v1/doctores/{id}/especialidades responde 201 al crear")
    void createEspecialidad_returnsCreated() throws Exception {
        Especialidad especialidad = new Especialidad();
        especialidad.setId(5L);
        especialidad.setNombre("Pediatría");
        especialidad.setDoctor(null);

        when(especialidadService.createForDoctor(2L, "Pediatría")).thenReturn(especialidad);

        EspecialidadRequest request = new EspecialidadRequest();
        request.setNombre("Pediatría");

        mockMvc.perform(post("/api/v1/doctores/{id}/especialidades", 2L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.nombre").value("Pediatría"))
            .andExpect(jsonPath("$.doctorId").doesNotExist());
    }

    @Test
    @DisplayName("POST /api/v1/doctores/{id}/especialidades responde 404 cuando el doctor no existe")
    void createEspecialidad_returnsNotFound() throws Exception {
        EspecialidadRequest request = new EspecialidadRequest();
        request.setNombre("Traumatología");

        when(especialidadService.createForDoctor(9L, "Traumatología"))
            .thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(post("/api/v1/doctores/{id}/especialidades", 9L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/doctores/{id}/especialidades responde 400 cuando falta nombre")
    void createEspecialidad_returnsBadRequest() throws Exception {
        EspecialidadRequest request = new EspecialidadRequest();
        request.setNombre(" ");

        mockMvc.perform(post("/api/v1/doctores/{id}/especialidades", 3L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/doctores/{id}/especialidades responde 204 cuando no hay registros")
    void getEspecialidadesByDoctor_returnsNoContent() throws Exception {
        when(especialidadService.findByDoctorId(5L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/doctores/{id}/especialidades", 5L))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/doctores/{id}/especialidades responde 404 cuando el doctor no existe")
    void getEspecialidadesByDoctor_returnsNotFound() throws Exception {
        when(especialidadService.findByDoctorId(7L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(get("/api/v1/doctores/{id}/especialidades", 7L))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/especialidades responde 200 con la lista completa")
    void getAllEspecialidades_returnsOk() throws Exception {
        Especialidad especialidad = new Especialidad();
        especialidad.setId(10L);
        especialidad.setNombre("Nefrología");

        when(especialidadService.findAll()).thenReturn(List.of(especialidad));

        mockMvc.perform(get("/api/v1/especialidades"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(10))
            .andExpect(jsonPath("$[0].nombre").value("Nefrología"));
    }

    @Test
    @DisplayName("GET /api/v1/especialidades/{id} devuelve 404 cuando no existe")
    void getEspecialidadById_returnsNotFound() throws Exception {
        when(especialidadService.findById(99L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(get("/api/v1/especialidades/{id}", 99L))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/especialidades crea con doctorId")
    void createEspecialidadGlobal_returnsCreated() throws Exception {
        Especialidad especialidad = new Especialidad();
        especialidad.setId(6L);
        especialidad.setNombre("Oncología");
        when(especialidadService.createForDoctor(3L, "Oncología")).thenReturn(especialidad);

        EspecialidadRequest request = new EspecialidadRequest();
        request.setNombre("Oncología");
        request.setDoctorId(3L);

        mockMvc.perform(post("/api/v1/especialidades")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(6))
            .andExpect(jsonPath("$.nombre").value("Oncología"));
    }

    @Test
    @DisplayName("PUT /api/v1/especialidades/{id} actualiza nombre")
    void updateEspecialidadGlobal_returnsOk() throws Exception {
        Especialidad especialidad = new Especialidad();
        especialidad.setId(7L);
        especialidad.setNombre("Dermato");
        when(especialidadService.update(7L, "Dermato", null)).thenReturn(especialidad);

        EspecialidadRequest request = new EspecialidadRequest();
        request.setNombre("Dermato");

        mockMvc.perform(put("/api/v1/especialidades/{id}", 7L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Dermato"));
    }

    @Test
    @DisplayName("DELETE /api/v1/especialidades/{id} responde 204")
    void deleteEspecialidadGlobal_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/especialidades/{id}", 4L))
            .andExpect(status().isNoContent());
    }
}
