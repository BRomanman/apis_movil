package com.clinica.api.citas.controller

import com.clinica.api.citas.model.Cita
import com.clinica.api.citas.service.CitaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/citas")
class CitaController(private val citaService: CitaService) {

    @GetMapping
    fun getAllCitas(): ResponseEntity<List<Cita>> {
        val citas = citaService.findAll()
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(citas)
    }

    @GetMapping("/{id}")
    fun getCitaById(@PathVariable id: Long): ResponseEntity<Cita> {
        return try {
            val cita = citaService.findById(id)
            ResponseEntity.ok(cita)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createCita(@RequestBody cita: Cita): ResponseEntity<Cita> {
        val nuevaCita = citaService.save(cita)
        return ResponseEntity(nuevaCita, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateCita(@PathVariable id: Long, @RequestBody citaDetails: Cita): ResponseEntity<Cita> {
        return try {
            val citaExistente = citaService.findById(id)
            citaExistente.fechaCita = citaDetails.fechaCita
            citaExistente.idDoctor = citaDetails.idDoctor
            citaExistente.idConsulta = citaDetails.idConsulta
            val citaActualizada = citaService.save(citaExistente)
            ResponseEntity.ok(citaActualizada)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteCita(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            citaService.findById(id) // Verifica que exista
            citaService.deleteById(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}