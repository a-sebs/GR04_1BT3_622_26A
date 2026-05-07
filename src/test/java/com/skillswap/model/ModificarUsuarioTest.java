package com.skillswap.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModificarUsuarioTest {

    @Test
    @DisplayName("given_usuario_intenta_cambiar_correo_a_formato_inválido_when_actualiza_then_no_se_cambia_el_correo")
    void given_user_attempts_to_change_email_to_invalid_format_when_updates_then_email_does_not_change() {
        Usuario usuario = new Usuario();
        usuario.setNombre("juan123");
        usuario.setPassword("password123");
        usuario.setCorreo("juan@example.com");
        String correoOriginal = usuario.getCorreo();

        String correoInvalido = "exampleexample.com"; // Sin @ (formato incorrecto)
        try {
            usuario.actualizarDatos(null, null, correoInvalido);
        } catch (IllegalArgumentException e) {
        }
        assertEquals(
                correoOriginal,
                usuario.getCorreo(),
                "El correo debe permanecer sin cambios cuando el formato es incorrecto"
        );
    }
}

