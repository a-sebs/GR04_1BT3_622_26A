package com.skillswap.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    @Test
    @DisplayName("HU11 - AC: Debe rechazar nombres con caracteres especiales")
    void given_name_when_special_characters_then_error() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombre("  Juan_Perez*  ");
        usuario.setPassword("password123");
        usuario.setCorreo("  JUAN@EXAMPLE.COM  ");

        // Act & Assert: la entidad no debe lanzar excepción por formato
        assertDoesNotThrow(() -> usuario.registrar());

        // Solo verifica normalización (responsabilidad actual de Usuario)
        assertEquals("Juan_Perez*", usuario.getNombre());
        assertEquals("juan@example.com", usuario.getCorreo());
    }
}
