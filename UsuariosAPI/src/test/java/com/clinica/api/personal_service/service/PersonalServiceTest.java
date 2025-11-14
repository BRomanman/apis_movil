package com.clinica.api.personal_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clinica.api.personal_service.model.Doctor;
import com.clinica.api.personal_service.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonalServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private PersonalService personalService;

    @Test
    @DisplayName("findDoctorById retorna el doctor cuando está activo")
    void findDoctorById_returnsEntity() {
        Doctor doctor = doctor(true);
        when(doctorRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(doctor));

        Doctor result = personalService.findDoctorById(1L);

        assertThat(result).isSameAs(doctor);
    }

    @Test
    @DisplayName("findDoctorById lanza EntityNotFoundException cuando no existe")
    void findDoctorById_throwsWhenMissing() {
        when(doctorRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalService.findDoctorById(1L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("saveDoctor asigna activo=true cuando no viene especificado")
    void saveDoctor_setsActiveDefault() {
        Doctor doctor = doctor(null);
        doctor.setActivo(null);
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Doctor saved = personalService.saveDoctor(doctor);

        assertThat(saved.getActivo()).isTrue();
        verify(doctorRepository).save(doctor);
    }

    @Test
    @DisplayName("deleteDoctorById marca el doctor como inactivo")
    void deleteDoctorById_marksInactive() {
        Doctor doctor = doctor(true);
        when(doctorRepository.findByIdAndActivoTrue(2L)).thenReturn(Optional.of(doctor));

        personalService.deleteDoctorById(2L);

        assertThat(doctor.getActivo()).isFalse();
        verify(doctorRepository).save(doctor);
    }

    private Doctor doctor(Boolean activo) {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setActivo(activo);
        return doctor;
    }
}
