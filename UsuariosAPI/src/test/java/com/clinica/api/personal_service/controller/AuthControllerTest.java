package com.clinica.api.personal_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.personal_service.dto.LoginRequest;
import com.clinica.api.personal_service.dto.LoginResponse;
import com.clinica.api.personal_service.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @SuppressWarnings("null")
    @Test
    @DisplayName("POST /api/v1/auth/login responde 200 con los datos del usuario")
    void login_returnsOk() throws Exception {
        LoginResponse response = new LoginResponse();
        response.setUserId(1L);
        response.setRole("paciente");
        when(usuarioService.login(any(LoginRequest.class))).thenReturn(response);

        LoginRequest request = new LoginRequest();
        request.setCorreo("correo@demo.com");
        request.setContrasena("clave");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1L));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("POST /api/v1/auth/login responde 401 cuando las credenciales no son válidas")
    void login_returnsUnauthorized() throws Exception {
        when(usuarioService.login(any(LoginRequest.class))).thenThrow(new EntityNotFoundException("No existe"));

        LoginRequest request = new LoginRequest();
        request.setCorreo("correo@demo.com");
        request.setContrasena("incorrecto");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
}
