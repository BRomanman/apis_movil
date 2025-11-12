package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonalService {

    private final DoctorRepository doctorRepository;

    public PersonalService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findAllDoctores() {
        return doctorRepository.findAll();
    }

    public Doctor findDoctorById(Long id) {
        return doctorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctorById(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor no encontrado para eliminar");
        }
        doctorRepository.deleteById(id);
    }
}
