package com.clinica.api.personal_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clinica.api.personal_service.dto.LoginRequest;
import com.clinica.api.personal_service.dto.LoginResponse;
import com.clinica.api.personal_service.dto.UsuarioResponse;
import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.model.Rol;
import com.clinica.api.personal_service.model.Usuario;
import com.clinica.api.personal_service.repository.DoctorRepository;
import com.clinica.api.personal_service.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("findAllUsuarios ignora los usuarios administradores")
    void findAllUsuarios_filtersAdmins() {
        Usuario user = usuario(1L, "Paciente");
        Usuario admin = usuario(2L, "Administrador");
        when(usuarioRepository.findAll()).thenReturn(List.of(user, admin));
        when(doctorRepository.findByUsuario_IdAndActivoTrue(1L)).thenReturn(Optional.empty());

        List<UsuarioResponse> responses = usuarioService.findAllUsuarios();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findUsuarioById lanza EntityNotFoundException para administradores")
    void findUsuarioById_blocksAdmins() {
        Usuario admin = usuario(3L, "Administrador");
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> usuarioService.findUsuarioById(3L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("saveUsuario lanza IllegalArgumentException cuando se intenta registrar un administrador")
    void saveUsuario_blocksAdminPayload() {
        Usuario admin = usuario(null, "Administrador");

        assertThatThrownBy(() -> usuarioService.saveUsuario(admin))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("login retorna información del doctor cuando aplica")
    void login_returnsDoctorInfo() {
        Usuario usuario = usuario(5L, "doctor");
        usuario.setCorreo("medico@demo.com");
        usuario.setContrasena("secreto");

        when(usuarioRepository.findByCorreo("medico@demo.com")).thenReturn(Optional.of(usuario));

        Doctor doctor = new Doctor();
        doctor.setId(9L);
        when(doctorRepository.findByUsuario_IdAndActivoTrue(5L)).thenReturn(Optional.of(doctor));

        LoginRequest request = new LoginRequest();
        request.setCorreo("medico@demo.com");
        request.setContrasena("secreto");

        LoginResponse response = usuarioService.login(request);

        assertThat(response.getDoctorId()).isEqualTo(9L);
        assertThat(response.getUserId()).isEqualTo(5L);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("deleteUsuarioById elimina al usuario cuando existe y no es administrador")
    void deleteUsuarioById_removesUsuario() {
        Usuario user = usuario(10L, "Paciente");
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(user));

        usuarioService.deleteUsuarioById(10L);

        verify(usuarioRepository).delete(user);
    }

    private Usuario usuario(Long id, String rolNombre) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Nombre");
        usuario.setApellido("Apellido");
        usuario.setFechaNacimiento(LocalDateTime.now());
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("clave");
        Rol rol = new Rol();
        rol.setNombre(rolNombre);
        usuario.setRol(rol);
        return usuario;
    }
}
