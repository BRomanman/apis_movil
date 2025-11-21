package com.clinica.api.historial_service.controller;

import com.clinica.api.historial_service.model.Historial;
import com.clinica.api.historial_service.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/historial")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Historial clínico")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtiene todos los historiales asociados a un usuario.")
    public ResponseEntity<List<Historial>> getHistorialesByUsuarioId(@PathVariable("usuarioId") Long usuarioId) {
        List<Historial> historiales = historialService.findHistorialesByUsuarioId(usuarioId);
        if (historiales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historiales);
    }

    @GetMapping("/{histId}")
    @Operation(summary = "Busca un historial por ID Historial.")
    public ResponseEntity<Historial> getHistorialById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(historialService.findHistorialById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
