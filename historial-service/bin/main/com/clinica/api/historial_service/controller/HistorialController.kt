package com.clinica.api.historial_service.controller

import com.clinica.api.historial_service.model.Historial
import com.clinica.api.historial_service.service.HistorialService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/historial")
class HistorialController(private val historialService: HistorialService) {


    @GetMapping("/usuario/{usuarioId}")
    fun getHistorialesByUsuarioId(@PathVariable usuarioId: Long): ResponseEntity<List<Historial>> {
        val historiales = historialService.findHistorialesByUsuarioId(usuarioId)
        return if (historiales.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(historiales)
        }
    }

    @GetMapping("/{id}")
    fun getHistorialById(@PathVariable id: Long): ResponseEntity<Historial> {
        return try {
            val historial = historialService.findHistorialById(id)
            ResponseEntity.ok(historial)
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}
