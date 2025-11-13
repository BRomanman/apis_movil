package com.clinica.api.seguros_service.service;

import com.clinica.api.seguros_service.dto.SeguroCancelRequest;
import com.clinica.api.seguros_service.dto.SeguroRequest;
import com.clinica.api.seguros_service.dto.SeguroResponse;
import com.clinica.api.seguros_service.dto.SeguroUpdateRequest;
import com.clinica.api.seguros_service.model.Seguro;
import com.clinica.api.seguros_service.model.SeguroEstado;
import com.clinica.api.seguros_service.model.Usuario;
import com.clinica.api.seguros_service.repository.SeguroRepository;
import com.clinica.api.seguros_service.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    public SeguroService(SeguroRepository seguroRepository, UsuarioRepository usuarioRepository) {
        this.seguroRepository = seguroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public SeguroResponse tomarSeguro(SeguroRequest request) {
        Usuario usuario = findUsuario(request.getUsuarioId());
        if (seguroRepository.existsByUsuarioIdAndNombreSeguroIgnoreCase(usuario.getId(), request.getNombreSeguro())) {
            throw new IllegalArgumentException("El usuario ya tiene un seguro con ese nombre");
        }

        Seguro seguro = new Seguro();
        seguro.setNombreSeguro(request.getNombreSeguro());
        seguro.setDescripcion(request.getDescripcion());
        seguro.setUsuario(usuario);
        seguro.setEstado(SeguroEstado.ACTIVO);
        seguro.setFechaCancelacion(null);

        return toResponse(seguroRepository.save(seguro));
    }

    public List<SeguroResponse> listarSeguros() {
        return seguroRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<SeguroResponse> listarSegurosPorUsuario(Long usuarioId) {
        return seguroRepository.findByUsuarioId(usuarioId).stream().map(this::toResponse).toList();
    }

    public SeguroResponse obtenerSeguro(Long id) {
        return toResponse(findSeguro(id));
    }

    @SuppressWarnings("null")
    public SeguroResponse actualizarSeguro(Long id, SeguroUpdateRequest request) {
        Seguro seguro = findSeguro(id);

        if (StringUtils.hasText(request.getNombreSeguro())) {
            boolean exists = seguroRepository.existsByUsuarioIdAndNombreSeguroIgnoreCase(
                seguro.getUsuario().getId(), request.getNombreSeguro());
            if (exists && !request.getNombreSeguro().equalsIgnoreCase(seguro.getNombreSeguro())) {
                throw new IllegalArgumentException("El usuario ya tiene un seguro con ese nombre");
            }
            seguro.setNombreSeguro(request.getNombreSeguro());
        }

        if (StringUtils.hasText(request.getDescripcion())) {
            seguro.setDescripcion(request.getDescripcion());
        }

        if (request.getUsuarioId() != null && !request.getUsuarioId().equals(seguro.getUsuario().getId())) {
            Usuario nuevoTitular = findUsuario(request.getUsuarioId());
            if (seguroRepository.existsByUsuarioIdAndNombreSeguroIgnoreCase(
                nuevoTitular.getId(), seguro.getNombreSeguro())) {
                throw new IllegalArgumentException("El nuevo titular ya tiene un seguro con ese nombre");
            }
            seguro.setUsuario(nuevoTitular);
        }

        return toResponse(seguroRepository.save(seguro));
    }

    public SeguroResponse cancelarSeguro(Long id, SeguroCancelRequest request) {
        Seguro seguro = findSeguro(id);
        if (seguro.getEstado() == SeguroEstado.CANCELADO) {
            throw new IllegalStateException("El seguro ya se encuentra cancelado");
        }

        seguro.setEstado(SeguroEstado.CANCELADO);
        seguro.setFechaCancelacion(LocalDateTime.now());

        if (request != null && StringUtils.hasText(request.getMotivo())) {
            String baseDescripcion = seguro.getDescripcion() == null ? "" : seguro.getDescripcion() + " ";
            seguro.setDescripcion(baseDescripcion + "[Cancelado: " + request.getMotivo().trim() + "]");
        }

        return toResponse(seguroRepository.save(seguro));
    }

    @SuppressWarnings("null")
    public void eliminarSeguro(Long id) {
        Seguro seguro = findSeguro(id);
        seguroRepository.delete(seguro);
    }

    @SuppressWarnings("null")
    private Seguro findSeguro(Long id) {
        return seguroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Seguro con id " + id + " no encontrado"));
    }

    @SuppressWarnings("null")
    private Usuario findUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + usuarioId + " no encontrado"));
    }

    private SeguroResponse toResponse(Seguro seguro) {
        Usuario usuario = seguro.getUsuario();
        String usuarioNombre = usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : null;
        return new SeguroResponse(
            seguro.getId(),
            seguro.getNombreSeguro(),
            seguro.getDescripcion(),
            seguro.getEstado().name(),
            seguro.getFechaCreacion(),
            seguro.getFechaCancelacion(),
            usuario != null ? usuario.getId() : null,
            usuarioNombre
        );
    }
}
