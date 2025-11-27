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
@Tag(name = "Doctores", description = "API para administrar la ficha laboral y remuneraciones de los médicos de la institución.")
public class DoctorController {

    private final PersonalService personalService;

    public DoctorController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    @Operation(
        summary = "Lista los doctores activos.",
        description = "Entrega el listado filtrado sólo con doctores vigentes e incluye los datos económicos y la especialidad principal."
    )
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
    @Operation(
        summary = "Obtiene la información del doctor por su ID.",
        description = "Devuelve el detalle completo de un doctor específico, incluyendo los datos del usuario asociado. "
            + "Si el ID no corresponde a un doctor activo, retorna 404."
    )
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable("id") Long id) {
        try {
            Doctor doctor = personalService.findDoctorById(id);
            return ResponseEntity.ok(mapToResponse(doctor));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
        summary = "Crea un nuevo doctor.",
        description = "Registra un profesional con su información contractual y de usuario, respondiendo 201 al persistirlo."
    )
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody Doctor doctor) {
        Doctor nuevoDoctor = personalService.saveDoctor(requireDoctorPayload(doctor));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nuevoDoctor));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualiza los datos económicos del doctor.",
        description = "Permite modificar tarifa, sueldo, bono y datos del usuario asociado de manera segura. "
            + "Cuando el médico no existe se responde 404."
    )
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
    @Operation(
        summary = "Marca como inactivo a un doctor.",
        description = "Realiza una baja lógica para impedir nuevas asignaciones, devolviendo 204 o 404 si el doctor no existe."
    )
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

        if (doctor.getEspecialidad() != null) {
            r.setEspecialidad(doctor.getEspecialidad().getNombre());
        }

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
