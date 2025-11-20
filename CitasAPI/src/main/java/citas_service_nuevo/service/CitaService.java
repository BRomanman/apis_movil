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
        if (cita.getId() == null) {
            cita.setEstado("CONFIRMADA");
        }
        return citaRepository.save(cita);
    }

    @SuppressWarnings("null")
    public void deleteById(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new EntityNotFoundException("Cita no encontrada");
        }
        citaRepository.deleteById(id);
    }

    public List<Cita> findByUsuario(Long idUsuario) {
        return citaRepository.findByUsuarioId(idUsuario);
    }

    // [NUEVO] Servicio para buscar por doctor
    public List<Cita> findByDoctor(Long idDoctor) {
        return citaRepository.findByDoctorId(idDoctor);
    }
}
