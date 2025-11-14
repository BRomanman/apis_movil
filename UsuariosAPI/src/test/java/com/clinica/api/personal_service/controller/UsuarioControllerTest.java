package com.clinica.api.personal_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinica.api.personal_service.dto.UsuarioResponse;
import com.clinica.api.personal_service.model.Rol;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("GET /api/v1/usuarios responde 200 con usuarios")
    void getAllUsuarios_returnsOk() throws Exception {
        UsuarioResponse response = usuarioResponse(1L);
        when(usuarioService.findAllUsuarios()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /api/v1/usuarios responde 204 cuando no existen datos")
    void getAllUsuarios_returnsNoContent() throws Exception {
        when(usuarioService.findAllUsuarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/usuarios"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/{id} responde 404 cuando el servicio lanza EntityNotFoundException")
    void getUsuarioById_returnsNotFound() throws Exception {
        when(usuarioService.findUsuarioById(9L)).thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(get("/api/v1/usuarios/{id}", 9L))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/usuarios responde 201 cuando se crea el usuario")
    void createUsuario_returnsCreated() throws Exception {
        Usuario request = usuario();
        Usuario saved = usuario();
        saved.setId(5L);
        UsuarioResponse response = usuarioResponse(5L);

        when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(saved);
        when(usuarioService.findUsuarioById(5L)).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    @DisplayName("PUT /api/v1/usuarios/{id} responde 404 cuando el usuario no existe")
    void updateUsuario_returnsNotFound() throws Exception {
        Usuario usuario = usuario();
        when(usuarioService.updateUsuario(any(Long.class), any(Usuario.class)))
            .thenThrow(new EntityNotFoundException("No existe"));

        mockMvc.perform(put("/api/v1/usuarios/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} responde 204 cuando se elimina")
    void deleteUsuario_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/usuarios/{id}", 3L))
            .andExpect(status().isNoContent());
    }

    private UsuarioResponse usuarioResponse(Long id) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(id);
        response.setNombre("Ana");
        response.setApellido("Pérez");
        response.setCorreo("ana@demo.com");
        return response;
    }

    private Usuario usuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setApellido("Pérez");
        usuario.setCorreo("ana@demo.com");
        usuario.setContrasena("clave");
        usuario.setFechaNacimiento(LocalDateTime.now());
        Rol rol = new Rol();
        rol.setNombre("Paciente");
        usuario.setRol(rol);
        return usuario;
    }
}
