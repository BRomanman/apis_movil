package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.dto.LoginRequest;
import com.clinica.api.personal_service.dto.LoginResponse;
import com.clinica.api.personal_service.dto.UsuarioResponse;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.repository.DoctorRepository;
import com.clinica.api.personal_service.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DoctorRepository doctorRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, DoctorRepository doctorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<UsuarioResponse> findAllUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public UsuarioResponse findUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return mapToResponse(usuario);
    }

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario changes) {
        Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        existente.setNombre(changes.getNombre());
        existente.setApellido(changes.getApellido());
        existente.setCorreo(changes.getCorreo());
        existente.setTelefono(changes.getTelefono());
        existente.setFechaNacimiento(changes.getFechaNacimiento());
        existente.setContrasena(changes.getContrasena());
        existente.setRol(changes.getRol());
        return usuarioRepository.save(existente);
    }

    public void deleteUsuarioById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado para eliminar");
        }
        usuarioRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<Usuario> opt = usuarioRepository.findByCorreo(request.getCorreo());
        if (opt.isEmpty()) {
            throw new EntityNotFoundException("Correo o contraseña incorrectos");
        }
        Usuario usuario = opt.get();
        if (usuario.getContrasena() == null || !usuario.getContrasena().equals(request.getContrasena())) {
            // En un entorno real usar hashing (BCrypt)
            throw new EntityNotFoundException("Correo o contraseña incorrectos");
        }
        LoginResponse resp = new LoginResponse();
        resp.setUserId(usuario.getId());
        resp.setRole(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        resp.setNombre(usuario.getNombre());
        resp.setApellido(usuario.getApellido());
        resp.setCorreo(usuario.getCorreo());

        doctorRepository.findByUsuario_Id(usuario.getId())
            .ifPresent(d -> resp.setDoctorId(d.getId()));

        return resp;
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        UsuarioResponse r = new UsuarioResponse();
        r.setId(usuario.getId());
        r.setNombre(usuario.getNombre());
        r.setApellido(usuario.getApellido());
        r.setFechaNacimiento(usuario.getFechaNacimiento());
        r.setCorreo(usuario.getCorreo());
        r.setTelefono(usuario.getTelefono());
        r.setRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);

        doctorRepository.findByUsuario_Id(usuario.getId()).ifPresent(doc -> {
            UsuarioResponse.DoctorInfo info = new UsuarioResponse.DoctorInfo();
            info.setId(doc.getId());
            info.setTarifaConsulta(doc.getTarifaConsulta());
            info.setSueldo(doc.getSueldo());
            info.setBono(doc.getBono());
            r.setDoctor(info);
        });

        return r;
    }
}

