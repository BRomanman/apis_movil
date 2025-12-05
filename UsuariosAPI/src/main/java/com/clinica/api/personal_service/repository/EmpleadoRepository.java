package com.clinica.api.personal_service.repository;

import com.clinica.api.personal_service.model.Empleado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByCorreoIgnoreCase(String correo);
}
