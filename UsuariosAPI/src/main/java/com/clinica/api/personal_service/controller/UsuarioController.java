package com.clinica.api.personal_service.controller;

import com.clinica.api.personal_service.dto.UsuarioCreateRequestDto;
import com.clinica.api.personal_service.dto.UsuarioResponseDto;
import com.clinica.api.personal_service.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearPaciente(@Valid @RequestBody UsuarioCreateRequestDto request) {
        UsuarioResponseDto response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(201).body(response);
    }
}
