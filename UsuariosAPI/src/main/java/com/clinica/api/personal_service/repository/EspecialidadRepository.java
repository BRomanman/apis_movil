package com.clinica.api.personal_service.repository;

import com.clinica.api.personal_service.model.Especialidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    List<Especialidad> findByDoctor_Id(Long doctorId);
}
