package citas_service_nuevo.service;

import citas_service_nuevo.model.Cita;
import citas_service_nuevo.repository.CitaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
            cita.setEstado("Disponible");
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
}
