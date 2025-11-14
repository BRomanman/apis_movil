package com.clinica.api.seguros_service.service;

import com.clinica.api.seguros_service.model.Seguro;
import com.clinica.api.seguros_service.model.SeguroEstado;
import com.clinica.api.seguros_service.repository.SeguroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class SeguroService {

    private final SeguroRepository seguroRepository;

    public SeguroService(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    public List<Seguro> findAll() {
        return seguroRepository.findAll();
    }

    public List<Seguro> findByUsuario(Long usuarioId) {
        return seguroRepository.findByUsuarioId(usuarioId);
    }

    public Seguro findById(Long id) {
        return seguroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Seguro no encontrado"));
    }

    public Seguro create(Seguro seguro) {
        seguro.setId(null);
        seguro.setEstado(SeguroEstado.ACTIVO);
        seguro.setFechaCancelacion(null);
        return seguroRepository.save(seguro);
    }

    public Seguro update(Long id, Seguro cambios) {
        Seguro existente = findById(id);
        existente.setNombreSeguro(cambios.getNombreSeguro());
        existente.setDescripcion(cambios.getDescripcion());
        existente.setUsuarioId(cambios.getUsuarioId());
        return seguroRepository.save(existente);
    }

    public Seguro cancel(Long id, String motivo) {
        Seguro seguro = findById(id);
        if (seguro.getEstado() == SeguroEstado.CANCELADO) {
            return seguro;
        }
        seguro.setEstado(SeguroEstado.CANCELADO);
        seguro.setFechaCancelacion(LocalDateTime.now());
        if (StringUtils.hasText(motivo)) {
            String base = seguro.getDescripcion() == null ? "" : seguro.getDescripcion() + " ";
            seguro.setDescripcion(base + "[Cancelado: " + motivo.trim() + "]");
        }
        return seguroRepository.save(seguro);
    }

    public void delete(Long id) {
        Seguro seguro = findById(id);
        seguroRepository.delete(seguro);
    }
}
