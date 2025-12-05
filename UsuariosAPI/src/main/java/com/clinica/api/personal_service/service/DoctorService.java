package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.dto.DoctorCreateRequestDto;
import com.clinica.api.personal_service.dto.DoctorResponseDto;

public interface DoctorService {

    DoctorResponseDto crearDoctor(DoctorCreateRequestDto request);
}
