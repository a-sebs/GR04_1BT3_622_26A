package com.skillswap.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.skillswap.model.Notificacion;
import org.junit.jupiter.api.BeforeEach;

class NotificacionServiceTest {

    private static final Long ID_NOTIFICACION = 1001L;
    private static final String MENSAJE = "Nueva solicitud de sesion";
    private static final boolean NO_LEIDA = false;

    private NotificacionService service;
    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        service = new NotificacionService();
        notificacion = new Notificacion(
                ID_NOTIFICACION,
                MENSAJE,
                new Date(),
                NO_LEIDA
        );
    }

    @Test
    @DisplayName("marcarLeida debe marcar la notificacion como leida al mostrar detalles")
    void given_unread_notification_when_marcar_leida_then_notification_is_marked_as_read() {
        // Arrange
        assertFalse(notificacion.isLeida());

        // Act
        service.marcarLeida(notificacion.getId());

        // Assert
        assertTrue(notificacion.isLeida());
    }
}
