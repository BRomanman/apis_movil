package com.clinica.api.personal_service.controller;

import com.clinica.api.personal_service.dto.DoctorResponse;
import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.service.PersonalService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctores")
public class DoctorController {

    private final PersonalService personalService;

    public DoctorController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctores() {
        List<Doctor> doctores = personalService.findAllDoctores();
        if (doctores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<DoctorResponse> response = doctores.stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = personalService.findDoctorById(id);
            return ResponseEntity.ok(mapToResponse(doctor));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody Doctor doctor) {
        Doctor nuevoDoctor = personalService.saveDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nuevoDoctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctorDetails) {
        try {
            Doctor existente = personalService.findDoctorById(id);
            existente.setTarifaConsulta(doctorDetails.getTarifaConsulta());
            existente.setSueldo(doctorDetails.getSueldo());
            existente.setBono(doctorDetails.getBono());
            existente.setUsuario(doctorDetails.getUsuario());
            Doctor actualizado = personalService.saveDoctor(existente);
            return ResponseEntity.ok(mapToResponse(actualizado));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        try {
            personalService.deleteDoctorById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private DoctorResponse mapToResponse(Doctor d) {
        DoctorResponse r = new DoctorResponse();
        r.setId(d.getId());
        r.setTarifaConsulta(d.getTarifaConsulta());
        r.setSueldo(d.getSueldo());
        r.setBono(d.getBono());
        Usuario u = d.getUsuario();
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
}

