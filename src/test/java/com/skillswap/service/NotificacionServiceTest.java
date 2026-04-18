package com.skillswap.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificacionServiceTest {

    private static final String SERVICE_CLASS = "com.skillswap.service.NotificacionService";

    @Test
    @DisplayName("debe declarar un metodo para crear la notificacion de solicitud")
    void debeDeclararMetodoParaCrearNotificacionDeSolicitud() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(SERVICE_CLASS));

        Method metodo = assertDoesNotThrow(() -> tipo.getDeclaredMethod(
                "crearNotificacionSolicitud",
                Long.class,
                Long.class,
                Long.class,
                String.class
        ));

        assertTrue(metodo.getReturnType().getSimpleName().contains("Notificacion"),
                "El metodo debe retornar una entidad o DTO de notificacion");
    }

    @Test
    @DisplayName("debe declarar un metodo para contar notificaciones no leidas por usuario destino")
    void debeDeclararMetodoParaContarNotificacionesNoLeidas() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(SERVICE_CLASS));

        Method metodo = assertDoesNotThrow(() -> tipo.getDeclaredMethod(
                "contarNotificacionesNoLeidas",
                Long.class
        ));

        assertTrue(metodo.getReturnType().getSimpleName().equals("Long")
                        || metodo.getReturnType().getSimpleName().equals("long")
                        || metodo.getReturnType().getSimpleName().equals("Integer"),
                "El contador debe devolver un valor numerico");
    }

    @Test
    @DisplayName("debe declarar un metodo para marcar como leidas las notificaciones del usuario destino")
    void debeDeclararMetodoParaMarcarTodasComoLeidas() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(SERVICE_CLASS));

        Method metodo = assertDoesNotThrow(() -> tipo.getDeclaredMethod(
                "marcarTodasComoLeidas",
                Long.class
        ));

        assertTrue(List.of("void", "Void").contains(metodo.getReturnType().getSimpleName()),
                "El metodo puede devolver void para indicar una accion de actualizacion");
    }
}


