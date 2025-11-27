package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonalService {

    private final DoctorRepository doctorRepository;

    public PersonalService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findAllDoctores() {
        List<Doctor> doctores = doctorRepository.findByActivoTrue();
        doctores.forEach(doctor -> doctor.getEspecialidad());
        return doctores;
    }

    public Doctor findDoctorById(Long id) {
        Doctor doctor = doctorRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
        doctor.getEspecialidad();
        return doctor;
    }

    public Doctor saveDoctor(Doctor doctor) {
        Doctor safeDoctor = Objects.requireNonNull(doctor, "Doctor entity must not be null");
        if (safeDoctor.getActivo() == null) {
            safeDoctor.setActivo(true);
        }
        return doctorRepository.save(safeDoctor);
    }

    public void deleteDoctorById(Long id) {
        Doctor doctor = findDoctorById(id);
        doctor.setActivo(false);
        doctorRepository.save(doctor);
    }
}
