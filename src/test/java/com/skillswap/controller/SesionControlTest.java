package com.skillswap.controller;

import com.skillswap.model.Sesion;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.SesionRepository;
import com.skillswap.repository.UsuarioRepository;
import com.skillswap.service.ValidadorDisponibilidad;
import com.skillswap.service.ValidadorDatos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SesionControlTest {

    @Mock
    @SuppressWarnings("unused")
    private UsuarioRepository usuarioRepository;
    @Mock
    @SuppressWarnings("unused")
    private MatchRepository matchRepository;
    @Mock
    private SesionRepository sesionRepository;
    @Mock
    @SuppressWarnings("unused")
    private ValidadorDisponibilidad validadorDisponibilidad;
    @Mock
    @SuppressWarnings("unused")
    private ValidadorDatos validadorDatos;

    @InjectMocks
    private SesionController sesionController;

    @ParameterizedTest
    @ValueSource(strings = {"sesion-uuid-1", "sesion-uuid-2", "sesion-uuid-3"})
    @DisplayName("Validar comportamiento de aceptarSesion para IDs válidos")
    void given_valid_session_id_when_aceptarSesion_then_ok(String id) {
        // Arrange
        Sesion sesion = crearSesion(id, LocalDate.now().plusDays(1));
        when(sesionRepository.findById(id)).thenReturn(Optional.of(sesion));
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        sesionController.aceptarSesion(id);

        // Assert
        assertEquals("FINALIZADA", sesion.getEstado());
        verify(sesionRepository).findById(id);
        verify(sesionRepository).save(sesion);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Validar que aceptarSesion falla con IDs vacíos")
    void given_blank_session_id_when_aceptarSesion_then_throw_illegal_argument(String id) {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class, () -> sesionController.aceptarSesion(id));
    }

    @Test
    @DisplayName("Validar que aceptarSesion falla cuando la sesión no existe")
    void given_non_existing_session_id_when_aceptarSesion_then_throw_illegal_argument() {
        // Arrange
        String id = "sesion-no-existe";
        when(sesionRepository.findById(id)).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> sesionController.aceptarSesion(id));
        verify(sesionRepository).findById(id);
    }

    @Test
    @DisplayName("Validar que confirmarDesision lanza excepción porque no hay decisión pendiente")
    void given_pending_decision_when_confirmarDesision_then_throw_illegal_state() {
        assertThrows(IllegalStateException.class, () -> sesionController.confirmarDesision());
    }

    @Test
    @DisplayName("Validar que confirmarDesision falla si no hay decisión pendiente")
    void given_no_pending_decision_when_confirmarDesision_then_throw_illegal_state() {
        // Arrange / Act / Assert
        assertThrows(IllegalStateException.class, () -> sesionController.confirmarDesision());
    }

    private Sesion crearSesion(String id, LocalDate fecha) {
        Sesion sesion = new Sesion();
        sesion.setId(id);
        sesion.setIdMatch("1");
        sesion.setHora("10:00");
        sesion.setFecha(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        sesion.setEstado("PENDIENTE");
        return sesion;
    }
}

