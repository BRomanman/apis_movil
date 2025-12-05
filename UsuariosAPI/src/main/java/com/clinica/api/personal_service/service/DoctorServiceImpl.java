package com.clinica.api.personal_service.service;

import com.clinica.api.personal_service.dto.DoctorCreateRequestDto;
import com.clinica.api.personal_service.dto.DoctorResponseDto;
import com.clinica.api.personal_service.exception.BusinessException;
import com.clinica.api.personal_service.exception.ResourceNotFoundException;
import com.clinica.api.personal_service.model.Empleado;
import com.clinica.api.personal_service.model.Especialidad;
import com.clinica.api.personal_service.model.Rol;
import com.clinica.api.personal_service.repository.EmpleadoRepository;
import com.clinica.api.personal_service.repository.EspecialidadRepository;
import com.clinica.api.personal_service.repository.RolRepository;
import jakarta.transaction.Transactional;
import java.util.Locale;
import org.springframework.stereotype.Service;


import org.springframework.security.crypto.password.PasswordEncoder;



@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private static final Long DOCTOR_ROLE_ID = 2L;
    private static final String TIPO_DOCTOR = "Doctor";

    private final EmpleadoRepository empleadoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(
        EmpleadoRepository empleadoRepository,
        EspecialidadRepository especialidadRepository,
        RolRepository rolRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.empleadoRepository = empleadoRepository;
        this.especialidadRepository = especialidadRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DoctorResponseDto crearDoctor(DoctorCreateRequestDto request) {
        if (request.getRol() == null || !DOCTOR_ROLE_ID.equals(request.getRol().getId())) {
            throw new BusinessException("Solo se permite crear doctores con rol 2");
        }
        if (!TIPO_DOCTOR.equalsIgnoreCase(request.getTipo())) {
            throw new BusinessException("El tipo debe ser 'Doctor'");
        }
        if (empleadoRepository.findByCorreoIgnoreCase(request.getCorreo()).isPresent()) {
            throw new BusinessException("Ya existe un empleado con ese correo");
        }

        Rol rol = rolRepository.findById(DOCTOR_ROLE_ID)
            .orElseThrow(() -> new ResourceNotFoundException("Rol Doctor no configurado"));
        Especialidad especialidad = especialidadRepository.findById(request.getEspecialidad().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada"));

        Empleado doctor = new Empleado();
        doctor.setNombre(request.getNombre().trim());
        doctor.setApellido(request.getApellido().trim());
        doctor.setFechaNacimiento(request.getFechaNacimiento());
        doctor.setCorreo(request.getCorreo().toLowerCase(Locale.ROOT));
        doctor.setTelefono(request.getTelefono());
        doctor.setContrasena(passwordEncoder.encode(request.getContrasena()));
        doctor.setRol(rol);
        doctor.setTipo(Empleado.Tipo.Doctor);
        doctor.setSueldo(request.getSueldo());
        doctor.setBono(request.getBono());
        doctor.setActivo(request.getActivo());
        doctor.setEspecialidad(especialidad);
        doctor.setTarifaConsulta(request.getTarifaConsulta());

        Empleado guardado = empleadoRepository.save(doctor);
        return mapToDto(guardado);
    }

    private DoctorResponseDto mapToDto(Empleado empleado) {
        DoctorResponseDto dto = new DoctorResponseDto();
        dto.setIdTrabajador(empleado.getIdTrabajador());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setCorreo(empleado.getCorreo());
        dto.setTelefono(empleado.getTelefono());
        dto.setActivo(empleado.getActivo());
        dto.setSueldo(empleado.getSueldo());
        dto.setBono(empleado.getBono());
        dto.setTarifaConsulta(empleado.getTarifaConsulta());
        if (empleado.getEspecialidad() != null) {
            dto.setEspecialidad(empleado.getEspecialidad().getNombre());
        }
        return dto;
    }
}
