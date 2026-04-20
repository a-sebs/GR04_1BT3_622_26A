package com.skillswap.service;

import com.skillswap.model.Notificacion;
import com.skillswap.repository.NotificacionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests de COMPORTAMIENTO para NotificacionService usando Mockito.
 *
 * Enfoque TDD: Pruebas unitarias con mocks para validar la lógica
 * de notificaciones sin depender de la base de datos.
 */
@ExtendWith(MockitoExtension.class)
class NotificacionServiceComportamientoTest {

    @Mock
    private NotificacionRepository notificacionRepository; // El "Actor de Reparto"

    @InjectMocks
    private NotificacionService notificacionService; // La clase a probar (SUT)

    // ==========================================
    // 4 TESTS UNITARIOS CLÁSICOS (Lógica de Estado)
    // ==========================================

    @Test
    @DisplayName("1. Debe asignar correctamente los valores base de la notificación")
    void debeAsignarValoresBaseCorrectamente() {
        // Arrange: Preparamos los datos
        when(notificacionRepository.save(any(Notificacion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // Act: Ejecutamos el método
        Notificacion resultado = notificacionService.crearNotificacionSolicitud(2L, 1L, 100L, "Hola");

        // Assert: Verificamos el estado
        assertNotNull(resultado, "La notificación no debe ser nula");
        assertEquals(2L, resultado.getUsuarioDestinoId(), "El usuario destino debe ser 2L");
        assertEquals(1L, resultado.getUsuarioEmisorId(), "El usuario emisor debe ser 1L");
        assertEquals(100L, resultado.getReferenciaSolicitudId(), "La referencia debe ser 100L");
        assertEquals("SOLICITUD_INTERCAMBIO", resultado.getTipo(), "El tipo debe ser SOLICITUD_INTERCAMBIO");
        assertEquals("Hola", resultado.getMensaje(), "El mensaje debe ser 'Hola'");
        assertFalse(resultado.getLeida(), "Por defecto debe estar sin leer (false)");
    }

    @Test
    @DisplayName("2. Debe retornar la cantidad correcta de notificaciones no leídas")
    void debeRetornarCantidadNoLeidas() {
        // Arrange
        Long usuarioId = 1L;
        when(notificacionRepository.countByUsuarioDestinoIdAndLeidaFalse(usuarioId))
                .thenReturn(5L);

        // Act
        Long cantidad = notificacionService.contarNotificacionesNoLeidas(usuarioId);

        // Assert
        assertEquals(5L, cantidad, "Debe retornar 5 notificaciones no leídas");
        verify(notificacionRepository, times(1))
                .countByUsuarioDestinoIdAndLeidaFalse(usuarioId);
    }

    @Test
    @DisplayName("3. Debe cambiar el estado 'leida' a true en una lista de notificaciones")
    void debeCambiarEstadoLeidaATrue() {
        // Arrange
        Notificacion n1 = new Notificacion();
        n1.setLeida(false);
        Notificacion n2 = new Notificacion();
        n2.setLeida(false);
        List<Notificacion> noLeidas = Arrays.asList(n1, n2);

        when(notificacionRepository.findByUsuarioDestinoIdAndLeidaFalse(1L))
                .thenReturn(noLeidas);
        when(notificacionRepository.saveAll(any()))
                .thenReturn(noLeidas);

        // Act
        notificacionService.marcarTodasComoLeidas(1L);

        // Assert
        assertTrue(n1.getLeida(), "Notificación 1 debe estar leída");
        assertTrue(n2.getLeida(), "Notificación 2 debe estar leída");
    }

    @Test
    @DisplayName("4. No debe fallar al intentar marcar como leídas si la lista está vacía")
    void noDebeFallarConListaVacia() {
        // Arrange
        when(notificacionRepository.findByUsuarioDestinoIdAndLeidaFalse(1L))
                .thenReturn(new ArrayList<>());
        when(notificacionRepository.saveAll(any()))
                .thenReturn(new ArrayList<>());

        // Act & Assert
        assertDoesNotThrow(
                () -> notificacionService.marcarTodasComoLeidas(1L),
                "El método debe manejar listas vacías sin lanzar excepciones"
        );
    }

    // ==========================================
    // 1 TEST UNITARIO CON PARÁMETROS
    // ==========================================

    @ParameterizedTest
    @CsvSource({
            "Hola me interesa, Hola me interesa",
            "'', ''"
    })
    @NullAndEmptySource
    @DisplayName("5. Debe manejar correctamente el texto del mensaje recibido")
    void debeManejarElMensaje(String mensajeEntrada, String mensajeEsperado) {
        // Arrange
        when(notificacionRepository.save(any(Notificacion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // Act
        Notificacion resultado = notificacionService.crearNotificacionSolicitud(
                2L, 1L, 100L, mensajeEntrada
        );

        // Assert
        // Si es null o vacío, debe convertirse a ""
        String esperado = (mensajeEntrada == null || mensajeEntrada.isEmpty())
                ? ""
                : mensajeEntrada;
        assertEquals(esperado, resultado.getMensaje(),
                "El mensaje debe ser procesado correctamente");
    }

    // ==========================================
    // 1 TEST UNITARIO CON MOCKITO (Verificación de Comportamiento)
    // ==========================================

    @Test
    @DisplayName("6. Debe llamar a los métodos find y saveAll del repositorio al marcar leídas")
    void debeInteractuarConRepositorioAlMarcarLeidas() {
        // Arrange
        List<Notificacion> listaMock = new ArrayList<>();
        Notificacion notif = new Notificacion();
        notif.setLeida(false);
        listaMock.add(notif);

        when(notificacionRepository.findByUsuarioDestinoIdAndLeidaFalse(1L))
                .thenReturn(listaMock);
        when(notificacionRepository.saveAll(any()))
                .thenReturn(listaMock);

        // Act
        notificacionService.marcarTodasComoLeidas(1L);

        // Assert: Validamos COMPORTAMIENTO, no solo resultados (clave de Mockito)
        // Verificamos que se consultó exactamente 1 vez
        verify(notificacionRepository, times(1))
                .findByUsuarioDestinoIdAndLeidaFalse(1L);

        // Verificamos que se guardó exactamente 1 vez
        verify(notificacionRepository, times(1))
                .saveAll(listaMock);

        // Verificamos que nunca se llamó sin parámetros (bonus)
        verify(notificacionRepository, never())
                .findAll();
    }
}

