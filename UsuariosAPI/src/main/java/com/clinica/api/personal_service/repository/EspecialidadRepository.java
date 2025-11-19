package com.clinica.api.personal_service.repository;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // [IMPORTANTE] Usamos List

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    // Usamos List para evitar errores si un doctor tiene más de una especialidad por error
    List<Especialidad> findByDoctor(Doctor doctor);
}