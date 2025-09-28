package citas_service_nuevo.controller

import citas_service_nuevo.model.Cita
import citas_service_nuevo.service.CitaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/citas")
class CitaController(private val citaService: CitaService) {

    @GetMapping
    fun getAllCitas(): ResponseEntity<List<Cita>> {
        val citas = citaService.findAll()
        if (citas.isEmpty()) { return ResponseEntity.noContent().build() }
        return ResponseEntity.ok(citas)
    }

    @GetMapping("/{id}")
    fun getCitaById(@PathVariable id: Long): ResponseEntity<Cita> {
        return try {
            ResponseEntity.ok(citaService.findById(id))
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    //Endpoint para ver todas las citas de un usuario
    @GetMapping("/usuario/{idUsuario}")
    fun getCitasByUsuario(@PathVariable idUsuario: Long): ResponseEntity<List<Cita>> {
        val citas = citaService.findByUsuario(idUsuario)
        if (citas.isEmpty()) { return ResponseEntity.noContent().build() }
        return ResponseEntity.ok(citas)
    }

    @PostMapping
    fun createCita(@RequestBody cita: Cita): ResponseEntity<Cita> {
        return ResponseEntity(citaService.save(cita), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateCita(@PathVariable id: Long, @RequestBody citaDetails: Cita): ResponseEntity<Cita> {
        return try {
            val citaExistente = citaService.findById(id)
            citaExistente.fechaCita = citaDetails.fechaCita
            citaExistente.doctor = citaDetails.doctor
            citaExistente.idConsulta = citaDetails.idConsulta
            ResponseEntity.ok(citaService.save(citaExistente))
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteCita(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            citaService.findById(id)
            citaService.deleteById(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    //Endpoint para la funcionalidad de recordatorios
    @GetMapping("/usuario/{idUsuario}/proximas")
    fun getProximasCitasByUsuario(@PathVariable idUsuario: Long): ResponseEntity<List<Cita>> {
        val citas = citaService.findProximasByUsuario(idUsuario)
        if (citas.isEmpty()) { return ResponseEntity.noContent().build() }
        return ResponseEntity.ok(citas)
    }
}