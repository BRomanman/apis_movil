package citas_service_nuevo.controller;

import citas_service_nuevo.model.Cita;
import citas_service_nuevo.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
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
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/v1/citas")
@Tag(name = "Citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    @Operation(summary = "Obtiene todas las citas registradas.")
    public ResponseEntity<List<Cita>> getAllCitas() {
        List<Cita> citas = citaService.findAll();
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una cita por su identificador.")
    public ResponseEntity<Cita> getCitaById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(citaService.findById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Lista las citas asociadas a un usuario.")
    public ResponseEntity<List<Cita>> getCitasByUsuario(@PathVariable("idUsuario") Long idUsuario) {
        List<Cita> citas = citaService.findByUsuario(idUsuario);
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }

    @PostMapping
    @Operation(summary = "Crea una nueva cita.")
    public ResponseEntity<Cita> createCita(@RequestBody Cita cita) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.save(cita));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza los datos principales de una cita.")
    public ResponseEntity<Cita> updateCita(@PathVariable("id") Long id, @RequestBody Cita citaDetails) {
        try {
            Cita existente = citaService.findById(id);
            existente.setFechaCita(citaDetails.getFechaCita());
            existente.setEstado(citaDetails.getEstado());
            existente.setIdUsuario(citaDetails.getIdUsuario());
            existente.setIdDoctor(citaDetails.getIdDoctor());
            existente.setPago(citaDetails.getPago());
            existente.setIdReceta(citaDetails.getIdReceta());
            existente.setIdResena(citaDetails.getIdResena());
            existente.setIdResumen(citaDetails.getIdResumen());
            existente.setIdConsulta(citaDetails.getIdConsulta());
            existente.setHoraInicio(citaDetails.getHoraInicio());
            existente.setHoraFin(citaDetails.getHoraFin());
            existente.setDuracionMinutos(citaDetails.getDuracionMinutos());
            existente.setDisponible(citaDetails.getDisponible());
            existente.setObservacionesHorario(citaDetails.getObservacionesHorario());
            return ResponseEntity.ok(citaService.save(existente));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}/proximas")
    @Operation(summary = "Recupera las próximas citas a partir de la fecha actual.")
    public ResponseEntity<List<Cita>> getProximasCitasByUsuario(@PathVariable("idUsuario") Long idUsuario) {
        List<Cita> citas = citaService.findProximasByUsuario(idUsuario);
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/doctor/{idDoctor}/proximas")
    @Operation(summary = "Recupera las próximas citas de un doctor a partir de la fecha actual.")
    public ResponseEntity<List<Cita>> getProximasCitasByDoctor(@PathVariable("idDoctor") Long idDoctor) {
        List<Cita> citas = citaService.findProximasByDoctor(idDoctor);
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/doctor/{idDoctor}/fecha/{fecha}")
    @Operation(summary = "Lista las citas de un doctor en una fecha específica (formato yyyy-MM-dd).")
    public ResponseEntity<List<Cita>> getCitasPorDoctorYFecha(
        @PathVariable("idDoctor") Long idDoctor,
        @PathVariable("fecha") String fecha
    ) {
        try {
            LocalDate fechaBusqueda = LocalDate.parse(fecha);
            List<Cita> citas = citaService.findByDoctorAndFecha(idDoctor, fechaBusqueda);
            if (citas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(citas);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/disponible")
    @Operation(summary = "Indica si una cita está disponible.")
    public ResponseEntity<Boolean> isCitaDisponible(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(citaService.isDisponible(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancela una cita (cambia estado a CANCELADO).")
    public ResponseEntity<Cita> cancelarCita(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(citaService.cancelarCita(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una cita existente.")
    public ResponseEntity<Void> deleteCita(@PathVariable("id") Long id) {
        try {
            citaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
