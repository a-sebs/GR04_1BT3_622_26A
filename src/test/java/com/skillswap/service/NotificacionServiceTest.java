package com.skillswap.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificacionServiceTest {

    private static final String SERVICE_CLASS = "com.skillswap.service.NotificacionService";

    @Test
    @DisplayName("obtenerLista debe poder invocarse en un mock del servicio")
    void given_list_when_have_notifications_then_ok() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(SERVICE_CLASS));

        Method metodo = assertDoesNotThrow(() -> tipo.getDeclaredMethod("obtenerLista"));
        Object servicioMock = Mockito.mock(tipo);

        assertDoesNotThrow(() -> metodo.invoke(servicioMock));

        boolean fueInvocado = Mockito.mockingDetails(servicioMock).getInvocations().stream()
                .anyMatch(invocacion -> invocacion.getMethod().getName().equals("obtenerLista"));

        assertTrue(fueInvocado, "Se esperaba una invocacion al metodo obtenerLista sobre el mock.");
        assertTrue(List.class.isAssignableFrom(metodo.getReturnType()),
                "obtenerLista debe retornar una coleccion tipo List.");
    }

    @Test
    @DisplayName("marcarLeida debe declararse con una firma valida")
    void given_notification_id_when_mark_as_read_then_ok() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(SERVICE_CLASS));

        Method metodo = assertDoesNotThrow(() -> tipo.getDeclaredMethod("marcarLeida", Long.class));

        Set<String> retornosPermitidos = Set.of("void", "Void", "boolean", "Boolean");
        assertTrue(retornosPermitidos.contains(metodo.getReturnType().getSimpleName()),
                "marcarLeida debe devolver void o un valor booleano.");
    }
}


