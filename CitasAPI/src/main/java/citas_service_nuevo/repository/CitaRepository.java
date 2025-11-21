package citas_service_nuevo.repository;

import citas_service_nuevo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByIdUsuario(Long idUsuario);

    List<Cita> findByIdUsuarioAndFechaCitaAfter(Long idUsuario, LocalDateTime fecha);

    List<Cita> findByIdDoctorAndFechaCitaBetweenOrderByHoraInicio(Long idDoctor, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Cita> findByIdDoctorAndFechaCitaAfter(Long idDoctor, LocalDateTime fecha);
}
