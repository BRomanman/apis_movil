package citas_service_nuevo.service;

import citas_service_nuevo.model.Cita;
import citas_service_nuevo.repository.CitaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @SuppressWarnings("null")
    public Cita findById(Long id) {
        return citaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));
    }

    public Cita save(Cita cita) {
        if (cita.getEstado() == null) {
            cita.setEstado("CONFIRMADA");
        }
        if (cita.getDisponible() == null) {
            cita.setDisponible(Boolean.TRUE);
        }
        return citaRepository.save(cita);
    }

    public List<Cita> findByUsuario(Long idUsuario) {
        return citaRepository.findByIdUsuario(idUsuario);
    }

    public List<Cita> findProximasByUsuario(Long idUsuario) {
        return citaRepository.findByIdUsuarioAndFechaCitaAfter(idUsuario, LocalDateTime.now());
    }

    public List<Cita> findProximasByDoctor(Long idDoctor) {
        return citaRepository.findByIdDoctorAndFechaCitaAfter(idDoctor, LocalDateTime.now());
    }

    public List<Cita> findByDoctorAndFecha(Long idDoctor, LocalDate fecha) {
        LocalDateTime inicioDelDia = fecha.atStartOfDay();
        LocalDateTime finDelDia = fecha.plusDays(1).atStartOfDay();
        return citaRepository.findByIdDoctorAndFechaCitaBetweenOrderByHoraInicio(idDoctor, inicioDelDia, finDelDia);
    }

    public boolean isDisponible(Long id) {
        return findById(id).getDisponible();
    }
    // [NUEVO] Método para cancelar sin borrar
    public Cita cancelarCita(Long id) {
        Cita cita = findById(id);
        cita.setEstado("CANCELADO");
        // Opcional: Si quieres que el horario quede libre de nuevo, pon disponible = true
        // cita.setDisponible(true); 
        return citaRepository.save(cita);
    }

    public void deleteById(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new EntityNotFoundException("Cita no encontrada");
        }
        citaRepository.deleteById(id);
    }
}
