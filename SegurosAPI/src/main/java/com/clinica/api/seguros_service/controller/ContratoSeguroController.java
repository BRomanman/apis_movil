package com.clinica.api.seguros_service.controller;

import com.clinica.api.seguros_service.model.ContratoSeguro;
import com.clinica.api.seguros_service.service.SeguroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seguros/contratos")
@Tag(name = "Contratos de Seguros")
public class ContratoSeguroController {

    private final SeguroService seguroService;

    public ContratoSeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtiene los contratos de seguro asociados a un usuario.")
    public ResponseEntity<List<ContratoSeguro>> listarContratosPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        List<ContratoSeguro> contratos = seguroService.findContratosByUsuario(idUsuario);
        if (contratos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/seguro/{idSeguro}")
    @Operation(summary = "Obtiene los contratos de un seguro específico.")
    public ResponseEntity<List<ContratoSeguro>> listarContratosPorSeguro(@PathVariable("idSeguro") Long idSeguro) {
        List<ContratoSeguro> contratos = seguroService.findContratosBySeguro(idSeguro);
        if (contratos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/{id_contrato}")
    @Operation(summary = "Obtiene un contrato por su ID.")
    public ResponseEntity<ContratoSeguro> obtenerContrato(@PathVariable("id_contrato") Long id) {
        try {
            return ResponseEntity.ok(seguroService.findContratoById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo contrato de seguro.")
    public ResponseEntity<ContratoSeguro> crearContrato(@RequestBody ContratoSeguro contrato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seguroService.createContrato(contrato));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancela un contrato de seguro.")
    public ResponseEntity<ContratoSeguro> cancelarContrato(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(seguroService.cancelarContrato(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
