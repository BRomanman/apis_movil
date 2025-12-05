package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.dto.UsuarioCreateRequestDto;
import com.clinica.api.personal_service.dto.UsuarioResponseDto;
import com.clinica.api.personal_service.exception.BusinessException;
import com.clinica.api.personal_service.exception.ResourceNotFoundException;
import com.clinica.api.personal_service.model.Rol;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.repository.RolRepository;
import com.clinica.api.personal_service.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioService {

    private static final Long PACIENTE_ROLE_ID = 1L;

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
        UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDto crearUsuario(UsuarioCreateRequestDto request) {
        if (usuarioRepository.findByCorreoIgnoreCase(request.getCorreo()).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese correo");
        }
        if (request.getRol() != null && !PACIENTE_ROLE_ID.equals(request.getRol().getId())) {
            throw new BusinessException("Solo se pueden crear pacientes (rol 1)");
        }

        Rol rol = rolRepository.findById(PACIENTE_ROLE_ID)
            .orElseThrow(() -> new ResourceNotFoundException("Rol Paciente no configurado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setApellido(request.getApellido().trim());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setCorreo(request.getCorreo().toLowerCase(Locale.ROOT));
        usuario.setTelefono(request.getTelefono());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(rol);

        Usuario saved = usuarioRepository.save(usuario);
        return mapToDto(saved);
    }

    private UsuarioResponseDto mapToDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        return dto;
    }
}
