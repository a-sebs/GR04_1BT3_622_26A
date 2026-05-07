package com.skillswap.model;

import com.skillswap.service.ValidadorDatos; // Importamos el validador
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModificarUsuarioTest {

    @Test
    @DisplayName("given_usuario_intenta_cambiar_correo_a_formato_inválido_when_actualiza_then_no_se_cambia_el_correo")
    void given_user_attempts_to_change_email_to_invalid_format_when_updates_then_email_does_not_change() {
        // Arrange: Instanciamos el validador para verificar la lógica de negocio desacoplada
        ValidadorDatos validador = new ValidadorDatos();
        Usuario usuario = new Usuario();
        usuario.setNombre("juan123");
        usuario.setPassword("password123");
        usuario.setCorreo("juan@example.com");
        String correoOriginal = usuario.getCorreo();

        String correoInvalido = "exampleexample.com"; // Sin @ (formato incorrecto)

        // Act: Simulamos el flujo del sistema (Controlador/Servicio)
        // 1. Primero validamos los datos usando el servicio especializado
        List<String> errores = validador.validarActualizacionPerfil(null, correoInvalido);

        // 2. Solo se procede con la actualización si no hay errores de validación
        if (errores.isEmpty()) {
            usuario.actualizarDatos(null, null, correoInvalido);
        }

        // Assert
        // Verificamos que el correo no cambió porque el flujo de validación lo impidió
        assertEquals(
                correoOriginal,
                usuario.getCorreo(),
                "El correo debe permanecer sin cambios porque el validador detectó el formato incorrecto"
        );

        // Verificamos que el validador capturó el error específico (CA de la HU11)
        assertTrue(errores.contains(ValidadorDatos.ERR_CORREO_FORMATO),
                "El validador debería haber identificado el error de formato de correo");
    }
}
