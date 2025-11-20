package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Especialidad;
import com.clinica.api.personal_service.repository.DoctorRepository;
import com.clinica.api.personal_service.repository.EspecialidadRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final DoctorRepository doctorRepository;

    public EspecialidadService(
        EspecialidadRepository especialidadRepository,
        DoctorRepository doctorRepository
    ) {
        this.especialidadRepository = especialidadRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Especialidad> findByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findByIdAndActivoTrue(doctorId)
            .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
        return especialidadRepository.findByDoctor_Id(doctor.getId());
    }
}
