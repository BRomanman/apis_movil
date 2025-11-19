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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioService {

    private static final String ADMIN_ROLE_NAME = "administrador";

    private final UsuarioRepository usuarioRepository;
    private final DoctorRepository doctorRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, DoctorRepository doctorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<UsuarioResponse> findAllUsuarios() {
        return usuarioRepository.findAll().stream()
            .filter(this::isAllowedUsuario)
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public UsuarioResponse findUsuarioById(Long id) {
        @SuppressWarnings("null")
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        ensureNotAdmin(usuario);
        return mapToResponse(usuario);
    }

    public Usuario saveUsuario(Usuario usuario) {
        Usuario safeUsuario = Objects.requireNonNull(usuario, "Usuario entity must not be null");
        ensurePayloadNotAdmin(safeUsuario);
        return usuarioRepository.save(safeUsuario);
    }

    public Usuario updateUsuario(Long id, Usuario changes) {
        @SuppressWarnings("null")
        Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        ensureNotAdmin(existente);
        Usuario safeChanges = Objects.requireNonNull(changes, "Usuario updates must not be null");
        ensurePayloadNotAdmin(safeChanges);

        // --- ACTUALIZACIÓN SEGURA ---
        // Solo actualizamos si el dato no es null.
        // Así evitamos borrar cosas por accidente.
        
        if (safeChanges.getNombre() != null) existente.setNombre(safeChanges.getNombre());
        if (safeChanges.getApellido() != null) existente.setApellido(safeChanges.getApellido());
        if (safeChanges.getCorreo() != null) existente.setCorreo(safeChanges.getCorreo());
        if (safeChanges.getTelefono() != null) existente.setTelefono(safeChanges.getTelefono());
        if (safeChanges.getFechaNacimiento() != null) existente.setFechaNacimiento(safeChanges.getFechaNacimiento());
        
        // CRÍTICO: Solo cambiamos la contraseña si viene una nueva
        if (safeChanges.getContrasena() != null && !safeChanges.getContrasena().isEmpty()) {
            existente.setContrasena(safeChanges.getContrasena());
        }
        
        if (safeChanges.getRol() != null) existente.setRol(safeChanges.getRol());

        return usuarioRepository.save(existente);
    }

    @SuppressWarnings("null")
    public void deleteUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para eliminar"));
        ensureNotAdmin(usuario);
        usuarioRepository.delete(usuario);
    }

    public LoginResponse login(LoginRequest request) {
        LoginRequest safeRequest = Objects.requireNonNull(request, "Credenciales requeridas");
        Optional<Usuario> opt = usuarioRepository.findByCorreo(safeRequest.getCorreo());
        if (opt.isEmpty()) {
            throw new EntityNotFoundException("Correo o contraseña incorrectos");
        }
        Usuario usuario = opt.get();
        if (usuario.getContrasena() == null || !usuario.getContrasena().equals(safeRequest.getContrasena())) {
            throw new EntityNotFoundException("Correo o contraseña incorrectos");
        }
        LoginResponse resp = new LoginResponse();
        resp.setUserId(usuario.getId());
        resp.setRole(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        resp.setNombre(usuario.getNombre());
        resp.setApellido(usuario.getApellido());
        resp.setCorreo(usuario.getCorreo());

        doctorRepository.findByUsuario_IdAndActivoTrue(usuario.getId())
            .ifPresent(d -> resp.setDoctorId(d.getId()));

        return resp;
    }

    private UsuarioResponse mapToResponse(Usuario usuarioInput) {
        Usuario usuario = Objects.requireNonNull(usuarioInput, "Usuario entity must not be null");
        UsuarioResponse r = new UsuarioResponse();
        r.setId(usuario.getId());
        r.setNombre(usuario.getNombre());
        r.setApellido(usuario.getApellido());
        r.setFechaNacimiento(usuario.getFechaNacimiento());
        r.setCorreo(usuario.getCorreo());
        r.setTelefono(usuario.getTelefono());
        r.setRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);

        doctorRepository.findByUsuario_IdAndActivoTrue(usuario.getId()).ifPresent(doc -> {
            UsuarioResponse.DoctorInfo info = new UsuarioResponse.DoctorInfo();
            info.setId(doc.getId());
            info.setTarifaConsulta(doc.getTarifaConsulta());
            info.setSueldo(doc.getSueldo());
            info.setBono(doc.getBono());
            r.setDoctor(info);
        });

        return r;
    }

    private void ensureNotAdmin(Usuario usuario) {
        if (isAdmin(usuario)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
    }

    private void ensurePayloadNotAdmin(Usuario usuario) {
        if (isAdmin(usuario)) {
            throw new IllegalArgumentException("No se permiten operaciones sobre administradores");
        }
    }

    private boolean isAllowedUsuario(Usuario usuario) {
        return usuario != null && !isAdmin(usuario);
    }

    private boolean isAdmin(Usuario usuario) {
        return usuario != null
            && usuario.getRol() != null
            && ADMIN_ROLE_NAME.equalsIgnoreCase(usuario.getRol().getNombre());
    }
}
