package com.clinica.api.personal_service.controller;

import com.clinica.api.personal_service.dto.DoctorCreateRequestDto;
import com.clinica.api.personal_service.dto.DoctorResponseDto;
import com.clinica.api.personal_service.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctores")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponseDto> crearDoctor(@Valid @RequestBody DoctorCreateRequestDto request) {
        DoctorResponseDto response = doctorService.crearDoctor(request);
        return ResponseEntity.status(201).body(response);
    }
}
