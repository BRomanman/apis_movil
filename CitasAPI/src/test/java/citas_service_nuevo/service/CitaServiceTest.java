package citas_service_nuevo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import citas_service_nuevo.model.Cita;
import citas_service_nuevo.repository.CitaRepository;
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
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaService citaService;

    @Test
    @DisplayName("findAll delega en el repositorio")
    void findAll_returnsRepositoryData() {
        List<Cita> citas = List.of(new Cita());
        when(citaRepository.findAll()).thenReturn(citas);

        List<Cita> result = citaService.findAll();

        assertThat(result).isEqualTo(citas);
        verify(citaRepository).findAll();
    }

    @Test
    @DisplayName("findById retorna la entidad cuando existe")
    void findById_returnsEntity() {
        Cita cita = new Cita();
        when(citaRepository.findById(5L)).thenReturn(Optional.of(cita));

        Cita result = citaService.findById(5L);

        assertThat(result).isSameAs(cita);
        verify(citaRepository).findById(5L);
    }

    @Test
    @DisplayName("findById lanza EntityNotFoundException cuando no existe la cita")
    void findById_throwsWhenMissing() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.findById(99L))
            .isInstanceOf(EntityNotFoundException.class);
        verify(citaRepository).findById(99L);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("save asigna estado CONFIRMADA cuando la cita es nueva")
    void save_setsDefaultStateOnCreate() {
        Cita cita = new Cita();
        cita.setFechaCita(LocalDateTime.now());
        when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cita result = citaService.save(cita);

        assertThat(result.getEstado()).isEqualTo("CONFIRMADA");
        verify(citaRepository).save(cita);
    }

    @Test
    @DisplayName("deleteById lanza EntityNotFoundException si la cita no existe")
    void deleteById_throwsWhenMissing() {
        when(citaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> citaService.deleteById(1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Cita no encontrada");
        verify(citaRepository).existsById(1L);
    }

    @Test
    @DisplayName("findProximasByUsuario delega en el repositorio con la fecha actual")
    void findProximasByUsuario_delegatesToRepository() {
        when(citaRepository.findByIdUsuarioAndFechaCitaAfter(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(List.of());

        citaService.findProximasByUsuario(7L);

        verify(citaRepository).findByIdUsuarioAndFechaCitaAfter(any(Long.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("findProximasByDoctor delega en el repositorio con la fecha actual")
    void findProximasByDoctor_delegatesToRepository() {
        when(citaRepository.findByIdDoctorAndFechaCitaAfter(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(List.of());

        citaService.findProximasByDoctor(3L);

        verify(citaRepository).findByIdDoctorAndFechaCitaAfter(any(Long.class), any(LocalDateTime.class));
    }
}
