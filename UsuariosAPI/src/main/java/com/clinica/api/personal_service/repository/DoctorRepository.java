package com.clinica.api.personal_service.repository;

import com.clinica.api.personal_service.model.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByActivoTrue();

    Optional<Doctor> findByIdAndActivoTrue(Long id);

    Optional<Doctor> findByUsuario_IdAndActivoTrue(Long usuarioId);
}
