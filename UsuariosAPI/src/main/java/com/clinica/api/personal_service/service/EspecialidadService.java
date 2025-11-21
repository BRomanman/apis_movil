package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Especialidad;
import com.clinica.api.personal_service.repository.DoctorRepository;
import com.clinica.api.personal_service.repository.EspecialidadRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
        return especialidadRepository.findByDoctor(doctor);
    }

    public Especialidad createForDoctor(Long doctorId, String nombreEspecialidad) {
        String nombre = Objects.requireNonNull(nombreEspecialidad, "El nombre de la especialidad es requerido");
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(nombre);
        especialidad.setDoctor(doctor);
        return especialidadRepository.save(especialidad);
    }

    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    public Especialidad findById(Long id) {
        return especialidadRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Especialidad no encontrada"));
    }

    public Especialidad update(Long id, String nombre, Long doctorId) {
        Especialidad especialidad = findById(id);
        if (nombre != null && !nombre.isBlank()) {
            especialidad.setNombre(nombre);
        }
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
            especialidad.setDoctor(doctor);
        }
        return especialidadRepository.save(especialidad);
    }

    public void delete(Long id) {
        if (!especialidadRepository.existsById(id)) {
            throw new EntityNotFoundException("Especialidad no encontrada");
        }
        especialidadRepository.deleteById(id);
    }
}
