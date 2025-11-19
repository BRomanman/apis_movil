package com.clinica.api.personal_service.controller;

import com.clinica.api.personal_service.dto.LoginRequest;
import com.clinica.api.personal_service.dto.LoginResponse;
import com.clinica.api.personal_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Autenticación")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(summary = "Valida las credenciales del usuario y retorna su rol.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginRequest safeRequest = Objects.requireNonNull(request, "Credenciales requeridas");
        try {
            LoginResponse resp = usuarioService.login(safeRequest);
            return ResponseEntity.ok(resp);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(401).build();
        }
    }
}

