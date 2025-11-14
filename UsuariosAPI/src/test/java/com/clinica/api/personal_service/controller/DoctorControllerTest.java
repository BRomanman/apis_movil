package com.clinica.api.personal_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Rol;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.service.PersonalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonalService personalService;

    @Test
    @DisplayName("GET /api/v1/doctores responde 200 con la lista de doctores")
    void getAllDoctores_returnsOk() throws Exception {
        when(personalService.findAllDoctores()).thenReturn(List.of(sampleDoctor()));

        mockMvc.perform(get("/api/v1/doctores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].tarifaConsulta").value(30000));
    }

    @Test
    @DisplayName("GET /api/v1/doctores responde 204 cuando no hay doctores")
    void getAllDoctores_returnsNoContent() throws Exception {
        when(personalService.findAllDoctores()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/doctores"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/doctores/{id} responde 404 cuando no existe el doctor")
    void getDoctorById_returnsNotFound() throws Exception {
        when(personalService.findDoctorById(8L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(get("/api/v1/doctores/{id}", 8L))
            .andExpect(status().isNotFound());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("POST /api/v1/doctores responde 201 con el registro creado")
    void createDoctor_returnsCreated() throws Exception {
        Doctor doctor = sampleDoctor();
        when(personalService.saveDoctor(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/api/v1/doctores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.usuario.nombre").value("Lucía"));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("PUT /api/v1/doctores/{id} responde 404 cuando el doctor no existe")
    void updateDoctor_returnsNotFound() throws Exception {
        Doctor doctor = sampleDoctor();
        when(personalService.findDoctorById(4L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(put("/api/v1/doctores/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/doctores/{id} responde 204")
    void deleteDoctor_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/doctores/{id}", 6L))
            .andExpect(status().isNoContent());
    }

    private Doctor sampleDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setTarifaConsulta(30000);
        doctor.setSueldo(1000000L);
        doctor.setBono(200000L);
        doctor.setUsuario(sampleUsuario());
        return doctor;
    }

    private Usuario sampleUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setNombre("Lucía");
        usuario.setApellido("Gómez");
        usuario.setCorreo("lucia@demo.com");
        usuario.setFechaNacimiento(LocalDateTime.now());
        usuario.setContrasena("pass");
        Rol rol = new Rol();
        rol.setNombre("Doctor");
        usuario.setRol(rol);
        return usuario;
    }
}
