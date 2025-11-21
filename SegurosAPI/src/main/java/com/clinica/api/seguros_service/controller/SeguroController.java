package com.clinica.api.seguros_service.controller;

import com.clinica.api.seguros_service.model.Seguro;
import com.clinica.api.seguros_service.service.SeguroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
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
@Tag(name = "Seguros")
public class SeguroController {

    private final SeguroService seguroService;

    public SeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    @GetMapping
    @Operation(summary = "Lista todos los seguros disponibles.")
    public ResponseEntity<List<Seguro>> listarSeguros() {
        List<Seguro> seguros = seguroService.findAll();
        if (seguros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(seguros);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Lista los seguros contratados por un usuario.")
    public ResponseEntity<List<Seguro>> listarPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        List<Seguro> seguros = seguroService.findByUsuario(usuarioId);
        if (seguros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(seguros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene los detalles de un seguro por su ID.")
    public ResponseEntity<Seguro> obtenerSeguro(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(seguroService.findById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo seguro para un usuario.")
    public ResponseEntity<Seguro> crearSeguro(@RequestBody Seguro seguro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seguroService.create(seguro));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza la información del seguro.")
    public ResponseEntity<Seguro> actualizarSeguro(
        @PathVariable("id") Long id,
        @RequestBody Seguro seguro
    ) {
        try {
            return ResponseEntity.ok(seguroService.update(id, seguro));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancelacion")
    @Operation(summary = "Cancela un seguro activo.")
    public ResponseEntity<Seguro> cancelarSeguro(
        @PathVariable("id") Long id,
        @RequestBody(required = false) Map<String, String> body
    ) {
        String motivo = body != null ? body.get("motivo") : null;
        try {
            return ResponseEntity.ok(seguroService.cancel(id, motivo));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina el registro de un seguro.")
    public ResponseEntity<Void> eliminarSeguro(@PathVariable("id") Long id) {
        try {
            seguroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
