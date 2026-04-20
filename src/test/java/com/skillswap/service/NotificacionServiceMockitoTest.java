package com.skillswap.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skillswap.model.Notificacion;
import com.skillswap.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceMockitoTest {

    private static final long ID_PENDIENTE_1 = 1L;
    private static final long ID_LEIDA = 2L;
    private static final long ID_PENDIENTE_2 = 3L;
    private static final String MENSAJE_PENDIENTE_1 = "Pendiente 1";
    private static final String MENSAJE_LEIDA = "Leida";
    private static final String MENSAJE_PENDIENTE_2 = "Pendiente 2";
    private static final int CANTIDAD_ESPERADA_PENDIENTES = 2;

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;
    private List<Notificacion> notificacionesMixtas;

    @BeforeEach
    void setUp() {
        Notificacion notificacionPendiente1 = new Notificacion(ID_PENDIENTE_1, MENSAJE_PENDIENTE_1, new Date(), false);
        Notificacion notificacionLeida = new Notificacion(ID_LEIDA, MENSAJE_LEIDA, new Date(), true);
        Notificacion notificacionPendiente2 = new Notificacion(ID_PENDIENTE_2, MENSAJE_PENDIENTE_2, new Date(), false);
        notificacionesMixtas = List.of(notificacionPendiente1, notificacionLeida, notificacionPendiente2);
    }

    @Test
    @DisplayName("solicitarLista debe devolver solo notificaciones pendientes (no leidas)")
    void given_mixed_notifications_when_solicitar_lista_then_returns_only_unread() {
        // Arrange
        when(notificacionRepository.findAll()).thenReturn(notificacionesMixtas);

        // Act
        List<Notificacion> resultado = notificacionService.solicitarLista();

        // Assert
        assertNotNull(resultado);
        assertEquals(CANTIDAD_ESPERADA_PENDIENTES, resultado.size());
        assertFalse(resultado.stream().anyMatch(Notificacion::isLeida));
        verify(notificacionRepository).findAll();
    }
}

