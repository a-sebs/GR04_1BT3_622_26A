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

    private static final Integer ID_PENDIENTE_1 = 1;
    private static final Integer ID_LEIDA = 2;
    private static final Integer ID_PENDIENTE_2 = 3;
    private static final String MENSAJE_PENDIENTE_1 = "Pendiente 1";
    private static final String MENSAJE_LEIDA = "Leida";
    private static final String MENSAJE_PENDIENTE_2 = "Pendiente 2";
    private static final String HABILIDADES = "Java, SQL";
    private static final int CANTIDAD_ESPERADA_PENDIENTES = 2;

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;
    private List<Notificacion> notificacionesMixtas;

    @BeforeEach
    void setUp() {
        Notificacion notificacionPendiente1 = crearNotificacion(ID_PENDIENTE_1, MENSAJE_PENDIENTE_1, false);
        Notificacion notificacionLeida = crearNotificacion(ID_LEIDA, MENSAJE_LEIDA, true);
        Notificacion notificacionPendiente2 = crearNotificacion(ID_PENDIENTE_2, MENSAJE_PENDIENTE_2, false);
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
        assertFalse(resultado.stream().anyMatch(Notificacion::isEstadoLectura));
        verify(notificacionRepository).findAll();
    }

    private Notificacion crearNotificacion(Integer id, String mensaje, boolean estadoLectura) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(id);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaPropuesta(new Date());
        notificacion.setHabilidades(HABILIDADES);
        notificacion.setEstadoLectura(estadoLectura);
        return notificacion;
    }
}
