package com.clinica.api.personal_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Especialidad;
import com.clinica.api.personal_service.service.EspecialidadService;
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
}
