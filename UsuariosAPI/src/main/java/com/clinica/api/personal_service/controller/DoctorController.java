package com.clinica.api.personal_service.controller;

import com.clinica.api.personal_service.dto.DoctorResponse;
import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.service.PersonalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctores")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Doctores")
public class DoctorController {

    private final PersonalService personalService;

    public DoctorController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    @Operation(summary = "Lista los doctores activos.")
    public ResponseEntity<List<DoctorResponse>> getAllDoctores() {
        List<Doctor> doctores = personalService.findAllDoctores();
        if (doctores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<DoctorResponse> response = doctores.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene la información del doctor por su ID.")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable("id") Long id) {
        try {
            Doctor doctor = personalService.findDoctorById(id);
            return ResponseEntity.ok(mapToResponse(doctor));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo doctor.")
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody Doctor doctor) {
        Doctor nuevoDoctor = personalService.saveDoctor(requireDoctorPayload(doctor));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nuevoDoctor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza los datos económicos del doctor.")
    public ResponseEntity<DoctorResponse> updateDoctor(
        @PathVariable("id") Long id,
        @RequestBody Doctor doctorDetails
    ) {
        try {
            Doctor existente = personalService.findDoctorById(id);
            Doctor safeDetails = requireDoctorPayload(doctorDetails);
            existente.setTarifaConsulta(safeDetails.getTarifaConsulta());
            existente.setSueldo(safeDetails.getSueldo());
            existente.setBono(safeDetails.getBono());
            existente.setUsuario(safeDetails.getUsuario());
            Doctor actualizado = personalService.saveDoctor(existente);
            return ResponseEntity.ok(mapToResponse(actualizado));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Marca como inactivo a un doctor.")
    public ResponseEntity<Void> deleteDoctor(@PathVariable("id") Long id) {
        try {
            personalService.deleteDoctorById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private DoctorResponse mapToResponse(Doctor doctorInput) {
        Doctor doctor = Objects.requireNonNull(doctorInput, "Doctor entity must not be null");
        DoctorResponse r = new DoctorResponse();
        r.setId(doctor.getId());
        r.setTarifaConsulta(doctor.getTarifaConsulta());
        r.setSueldo(doctor.getSueldo());
        r.setBono(doctor.getBono());
        Usuario u = doctor.getUsuario();
        if (u != null) {
            DoctorResponse.UsuarioInfo ui = new DoctorResponse.UsuarioInfo();
            ui.setId(u.getId());
            ui.setNombre(u.getNombre());
            ui.setApellido(u.getApellido());
            ui.setFechaNacimiento(u.getFechaNacimiento());
            ui.setCorreo(u.getCorreo());
            ui.setTelefono(u.getTelefono());
            ui.setRol(u.getRol() != null ? u.getRol().getNombre() : null);
            r.setUsuario(ui);
        }
        return r;
    }

    private Doctor requireDoctorPayload(Doctor doctor) {
        return Objects.requireNonNull(doctor, "Doctor payload must not be null");
    }
}
