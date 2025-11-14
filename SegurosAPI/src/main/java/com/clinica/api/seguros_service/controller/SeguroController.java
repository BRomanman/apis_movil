package com.clinica.api.seguros_service.controller;

import com.clinica.api.seguros_service.dto.SeguroCancelRequest;
import com.clinica.api.seguros_service.dto.SeguroRequest;
import com.clinica.api.seguros_service.dto.SeguroResponse;
import com.clinica.api.seguros_service.dto.SeguroUpdateRequest;
import com.clinica.api.seguros_service.service.SeguroService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seguros")
public class SeguroController {

    private final SeguroService seguroService;

    public SeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    @PostMapping
    public ResponseEntity<SeguroResponse> tomarSeguro(@Valid @RequestBody SeguroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seguroService.tomarSeguro(request));
    }

    @GetMapping
    public ResponseEntity<List<SeguroResponse>> listarSeguros() {
        List<SeguroResponse> seguros = seguroService.listarSeguros();
        if (seguros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(seguros);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SeguroResponse>> listarPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        List<SeguroResponse> seguros = seguroService.listarSegurosPorUsuario(usuarioId);
        if (seguros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(seguros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguroResponse> obtenerSeguro(@PathVariable("id") Long id) {
        return ResponseEntity.ok(seguroService.obtenerSeguro(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeguroResponse> actualizarSeguro(
        @PathVariable("id") Long id,
        @Valid @RequestBody SeguroUpdateRequest request
    ) {
        return ResponseEntity.ok(seguroService.actualizarSeguro(id, request));
    }

    @PatchMapping("/{id}/cancelacion")
    public ResponseEntity<SeguroResponse> cancelarSeguro(
        @PathVariable("id") Long id,
        @Valid @RequestBody(required = false) SeguroCancelRequest request
    ) {
        return ResponseEntity.ok(seguroService.cancelarSeguro(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSeguro(@PathVariable("id") Long id) {
        seguroService.eliminarSeguro(id);
        return ResponseEntity.noContent().build();
    }
}
