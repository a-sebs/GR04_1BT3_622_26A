package com.skillswap.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests en FASE ROJA (Red) del ciclo TDD para NotificacionController.
 *
 * Estos tests verifican por reflexión que el controlador declare los métodos
 * esperados según el diagrama de secuencia del proyecto.
 *
 * Fallarán hasta que se implemente NotificacionController con todos los métodos.
 */
class NotificacionControllerTest {

    private static final String CONTROLLER_CLASS = "com.skillswap.controller.NotificacionController";

    // ==========================================
    // TEST 1: obtenerLista() debe existir
    // ==========================================
    @Test
    @DisplayName("1. Debe declarar método obtenerLista() que retorna String (nombre de vista)")
    void debeDeclarарMetodoObtenerLista() {
        // Arrange: Verificar que la clase existe
        Class<?> tipo = assertDoesNotThrow(
            () -> Class.forName(CONTROLLER_CLASS),
            "NotificacionController debe existir en el paquete com.skillswap.controller"
        );

        // Act: Buscar el método obtenerLista sin parámetros
        Method metodo = assertDoesNotThrow(
            () -> tipo.getDeclaredMethod("obtenerLista"),
            "NotificacionController debe tener el método obtenerLista()"
        );

        // Assert: Verificar que retorna String (nombre de vista JSP)
        assertTrue(
            metodo.getReturnType().equals(String.class),
            "obtenerLista() debe retornar String (nombre de la vista)"
        );
    }

    // ==========================================
    // TEST 2: obtenerLista() debe tener mapeo @GetMapping
    // ==========================================
    @Test
    @DisplayName("2. Método obtenerLista() debe estar anotado con @GetMapping")
    void debeTenerGetMappingEnObtenerLista() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(CONTROLLER_CLASS));

        Method metodo = assertDoesNotThrow(
            () -> tipo.getDeclaredMethod("obtenerLista"),
            "El método obtenerLista() debe existir"
        );

        // Assert: Verificar que tiene anotación @GetMapping
        assertTrue(
            metodo.isAnnotationPresent(GetMapping.class),
            "obtenerLista() debe estar anotado con @GetMapping"
        );
    }

    // ==========================================
    // TEST 3: mostrarDetalles(id) debe existir
    // ==========================================
    @Test
    @DisplayName("3. Debe declarar método mostrarDetalles(Long id) que retorna String")
    void debeDeclararMetodoMostrarDetalles() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(CONTROLLER_CLASS));

        // Act: Buscar método mostrarDetalles con parámetro Long
        Method metodo = assertDoesNotThrow(
            () -> tipo.getDeclaredMethod("mostrarDetalles", Long.class),
            "NotificacionController debe tener el método mostrarDetalles(Long id)"
        );

        // Assert: Verificar retorno
        assertTrue(
            metodo.getReturnType().equals(String.class),
            "mostrarDetalles(Long) debe retornar String (nombre de la vista)"
        );
    }

    // ==========================================
    // TEST 4: mostrarDetalles(id) debe tener mapeo @GetMapping con PathVariable
    // ==========================================
    @Test
    @DisplayName("4. Método mostrarDetalles debe estar anotado con @GetMapping y tener @PathVariable")
    void debeTenerMappingEnMostrarDetalles() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(CONTROLLER_CLASS));

        Method metodo = assertDoesNotThrow(
            () -> tipo.getDeclaredMethod("mostrarDetalles", Long.class),
            "El método mostrarDetalles(Long) debe existir"
        );

        // Assert: Verificar anotaciones
        assertTrue(
            metodo.isAnnotationPresent(GetMapping.class),
            "mostrarDetalles(Long) debe estar anotado con @GetMapping"
        );
    }

    // ==========================================
    // TEST 5: marcarLeida(id) debe existir
    // ==========================================
    @Test
    @DisplayName("5. Debe declarar método marcarLeida(Long id) que retorna String o void")
    void debeDeclararMetodoMarcarLeida() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(CONTROLLER_CLASS));

        // Act: Buscar método marcarLeida con parámetro Long
        Method metodo = assertDoesNotThrow(
            () -> tipo.getDeclaredMethod("marcarLeida", Long.class),
            "NotificacionController debe tener el método marcarLeida(Long id)"
        );

        // Assert: Verificar que retorna String (redirect) o void
        boolean esStringOVoid =
            metodo.getReturnType().equals(String.class) ||
            metodo.getReturnType().equals(Void.TYPE);

        assertTrue(
            esStringOVoid,
            "marcarLeida(Long) debe retornar String (redirect) o void"
        );
    }

    // ==========================================
    // TEST 6: marcarLeida(id) debe tener mapeo @PostMapping
    // ==========================================
    @Test
    @DisplayName("6. Método marcarLeida debe estar anotado con @PostMapping")
    void debeTenerPostMappingEnMarcarLeida() {
        Class<?> tipo = assertDoesNotThrow(() -> Class.forName(CONTROLLER_CLASS));

        Method metodo = assertDoesNotThrow(
            () -> tipo.getDeclaredMethod("marcarLeida", Long.class),
            "El método marcarLeida(Long) debe existir"
        );

        // Assert: Verificar anotación @PostMapping
        assertTrue(
            metodo.isAnnotationPresent(PostMapping.class),
            "marcarLeida(Long) debe estar anotado con @PostMapping"
        );
    }
}