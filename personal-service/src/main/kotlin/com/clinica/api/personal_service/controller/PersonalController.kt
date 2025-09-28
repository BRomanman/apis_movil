package com.clinica.api.personal_service.controller

import com.clinica.api.personal_service.model.Doctor
import com.clinica.api.personal_service.service.PersonalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/personal")
class PersonalController(private val personalService: PersonalService) {

    @GetMapping
    fun getAllDoctores(): ResponseEntity<List<Doctor>> {
        val doctores = personalService.findAllDoctores()
        return if (doctores.isNotEmpty()) {
            ResponseEntity.ok(doctores)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @GetMapping("/{id}")
    fun getDoctorById(@PathVariable id: Long): ResponseEntity<Doctor> {
        return try {
            val doctor = personalService.findDoctorById(id)
            ResponseEntity.ok(doctor)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createDoctor(@RequestBody doctor: Doctor): ResponseEntity<Doctor> {
        val nuevoDoctor = personalService.saveDoctor(doctor)
        return ResponseEntity(nuevoDoctor, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateDoctor(@PathVariable id: Long, @RequestBody doctorDetails: Doctor): ResponseEntity<Doctor> {
        return try {
            val doctorExistente = personalService.findDoctorById(id)
            // Actualizamos los campos que se pueden modificar
            doctorExistente.sueldo = doctorDetails.sueldo
            doctorExistente.usuario = doctorDetails.usuario // Se asume que el objeto usuario viene completo
            val doctorActualizado = personalService.saveDoctor(doctorExistente)
            ResponseEntity.ok(doctorActualizado)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteDoctor(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            personalService.deleteDoctorById(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}