package com.clinica.api.personal_service.controller;

import com.clinica.api.personal_service.dto.EspecialidadResponse;
import com.clinica.api.personal_service.model.Especialidad;
import com.clinica.api.personal_service.service.EspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctores")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping("/{doctorId}/especialidades")
    @Operation(summary = "Lista las especialidades asociadas a un doctor.")
    public ResponseEntity<List<EspecialidadResponse>> getEspecialidadesByDoctor(
        @PathVariable("doctorId") Long doctorId
    ) {
        try {
            List<Especialidad> especialidades = especialidadService.findByDoctorId(doctorId);
            if (especialidades.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<EspecialidadResponse> response = especialidades.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private EspecialidadResponse mapToResponse(Especialidad especialidadInput) {
        Especialidad especialidad = Objects.requireNonNull(especialidadInput, "Especialidad entity must not be null");
        EspecialidadResponse response = new EspecialidadResponse();
        response.setId(especialidad.getId());
        response.setNombre(especialidad.getNombre());
        response.setDoctorId(especialidad.getDoctor() != null ? especialidad.getDoctor().getId() : null);
        return response;
    }
}
