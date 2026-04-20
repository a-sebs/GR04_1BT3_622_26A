package com.skillswap.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skillswap.model.Notificacion;
import com.skillswap.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    private static final Integer ID_NOTIFICACION = 1001;
    private static final String MENSAJE = "Nueva solicitud de sesion";
    private static final String HABILIDADES = "Java, SQL";
    private static final boolean NO_LEIDA = false;

    @Mock
    private NotificacionRepository notificacionRepository;

    private NotificacionService service;
    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        service = new NotificacionService(notificacionRepository);
        notificacion = new Notificacion();
        notificacion.setIdNotificacion(ID_NOTIFICACION);
        notificacion.setMensaje(MENSAJE);
        notificacion.setFechaPropuesta(new Date());
        notificacion.setHabilidades(HABILIDADES);
        notificacion.setEstadoLectura(NO_LEIDA);
    }

    @Test
    @DisplayName("marcarLeida debe marcar la notificacion como leida al mostrar detalles")
    void given_unread_notification_when_marcar_leida_then_notification_is_marked_as_read() {
        // Arrange
        when(notificacionRepository.findById(ID_NOTIFICACION)).thenReturn(Optional.of(notificacion));
        assertFalse(notificacion.isEstadoLectura());

        // Act
        service.marcarLeida(notificacion.getIdNotificacion().longValue());

        // Assert
        assertTrue(notificacion.isEstadoLectura());
        verify(notificacionRepository).save(notificacion);
    }
}
