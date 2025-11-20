package citas_service_nuevo.repository;

import citas_service_nuevo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Buscamos citas por el ID del Usuario
    List<Cita> findByUsuarioId(Long idUsuario);
    
    // [NUEVO] Buscamos citas por el ID del Doctor
    List<Cita> findByDoctorId(Long idDoctor);
}