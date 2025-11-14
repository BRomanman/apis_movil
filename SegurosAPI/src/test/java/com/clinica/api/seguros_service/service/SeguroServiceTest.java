package com.clinica.api.seguros_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clinica.api.seguros_service.model.Seguro;
import com.clinica.api.seguros_service.model.SeguroEstado;
import com.clinica.api.seguros_service.repository.SeguroRepository;
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
class SeguroServiceTest {

    @Mock
    private SeguroRepository seguroRepository;

    @InjectMocks
    private SeguroService seguroService;

    @Test
    @DisplayName("findAll delega en el repositorio")
    void findAll_returnsRepositoryData() {
        when(seguroRepository.findAll()).thenReturn(List.of(nuevoSeguro()));

        List<Seguro> seguros = seguroService.findAll();

        assertThat(seguros).hasSize(1);
        verify(seguroRepository).findAll();
    }

    @Test
    @DisplayName("findById lanza EntityNotFoundException cuando no existe")
    void findById_throwsWhenMissing() {
        when(seguroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seguroService.findById(1L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("create establece estado y fecha de cancelación por defecto")
    void create_setsDefaults() {
        Seguro request = nuevoSeguro();
        request.setEstado(null);
        request.setFechaCancelacion(LocalDateTime.now());

        when(seguroRepository.save(any(Seguro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Seguro created = seguroService.create(request);

        assertThat(created.getEstado()).isEqualTo(SeguroEstado.ACTIVO);
        assertThat(created.getFechaCancelacion()).isNull();
        verify(seguroRepository).save(request);
    }

    @Test
    @DisplayName("update reemplaza los datos principales del seguro")
    void update_changesFields() {
        Seguro existente = nuevoSeguro();
        when(seguroRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(seguroRepository.save(any(Seguro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Seguro cambios = nuevoSeguro();
        cambios.setNombreSeguro("Actualizado");
        cambios.setDescripcion("Desc");
        cambios.setUsuarioId(9L);

        Seguro actualizado = seguroService.update(5L, cambios);

        assertThat(actualizado.getNombreSeguro()).isEqualTo("Actualizado");
        assertThat(actualizado.getUsuarioId()).isEqualTo(9L);
        verify(seguroRepository).save(existente);
    }

    @Test
    @DisplayName("cancel cambia el estado y agrega motivo si se proporciona")
    void cancel_updatesState() {
        Seguro seguro = nuevoSeguro();
        when(seguroRepository.findById(3L)).thenReturn(Optional.of(seguro));
        when(seguroRepository.save(any(Seguro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Seguro cancelado = seguroService.cancel(3L, "Fin de contrato");

        assertThat(cancelado.getEstado()).isEqualTo(SeguroEstado.CANCELADO);
        assertThat(cancelado.getDescripcion()).contains("Fin de contrato");
        verify(seguroRepository).save(seguro);
    }

    private Seguro nuevoSeguro() {
        Seguro seguro = new Seguro();
        seguro.setId(1L);
        seguro.setNombreSeguro("Dental");
        seguro.setDescripcion("Cobertura dental");
        seguro.setEstado(SeguroEstado.ACTIVO);
        seguro.setUsuarioId(1L);
        return seguro;
    }
}
