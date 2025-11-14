package citas_service_nuevo.controller;

import citas_service_nuevo.model.Cita;
import citas_service_nuevo.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
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
            existente.setDoctor(citaDetails.getDoctor());
            existente.setIdConsulta(citaDetails.getIdConsulta());
            return ResponseEntity.ok(citaService.save(existente));
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

    @GetMapping("/usuario/{idUsuario}/proximas")
    @Operation(summary = "Recupera las próximas citas a partir de la fecha actual.")
    public ResponseEntity<List<Cita>> getProximasCitasByUsuario(@PathVariable("idUsuario") Long idUsuario) {
        List<Cita> citas = citaService.findProximasByUsuario(idUsuario);
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }
}
