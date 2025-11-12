package citas_service_nuevo.repository;

import citas_service_nuevo.model.Cita;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByUsuarioId(Long idUsuario);

    List<Cita> findByUsuarioIdAndFechaCitaAfter(Long idUsuario, LocalDateTime fecha);
}
