package com.skillswap.service;

import com.skillswap.controller.SesionController;
import com.skillswap.model.Usuario;
import com.skillswap.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecuperacionServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SesionController sesionController; // Clase donde implementaremos la lógica

    @Test
    @DisplayName("CA1: Debe permitir la recuperación si el correo existe en el repositorio")
    void given_mail_when_asked_new_pass_then_ok() {
        // Arrange: Preparamos un usuario simulado
        String correoValido = "ana@skillswap.com";
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setId(1L);
        usuarioSimulado.setCorreo(correoValido);

        // Configuramos el Mock para que devuelva el usuario dentro de un Optional
        when(usuarioRepository.findByCorreoIgnoreCase(correoValido))
                .thenReturn(Optional.of(usuarioSimulado));

        // Act: Ejecutamos la lógica interna
        boolean resultado = sesionController.procesarRecuperacion(correoValido);

        // Assert: El sistema debe confirmar que procedió con éxito
        assertTrue(resultado, "El sistema debería permitir el avance si el correo está registrado");

        // Verificamos que se consultó al repositorio exactamente una vez
        verify(usuarioRepository, times(1)).findByCorreoIgnoreCase(correoValido);
    }

    @Test
    @DisplayName("CA2: Debe informar error si el correo no existe en la base de datos")
    void given_mail_when_does_not_exist_then_error() {
        // Arrange: Configuramos el Mock para que devuelva un Optional vacío (usuario no encontrado)
        String correoInexistente = "no-existe@skillswap.com";
        when(usuarioRepository.findByCorreoIgnoreCase(correoInexistente)).thenReturn(Optional.empty());

        // Act: Intentamos el flujo de recuperación
        // (Este método aún no existe, por eso es FASE ROJA)
        boolean resultado = sesionController.procesarRecuperacion(correoInexistente);

        // Assert: Verificamos que el sistema devuelva false y que se buscó en el repositorio
        assertFalse(resultado, "El sistema debería rechazar la solicitud si el correo no existe");
        verify(usuarioRepository).findByCorreoIgnoreCase(correoInexistente);
    }

    @Test
    @DisplayName("CA3: Debe lanzar excepción si la nueva contraseña es menor a 8 caracteres")
    void given_short_pass_when_registering_then_fail() {
        // Arrange: Creamos un usuario real para probar su lógica interna
        Usuario usuario = new Usuario();
        usuario.setNombre("UserTest");
        usuario.setCorreo("test@skillswap.com");

        // Act & Assert: Intentamos registrar con una clave de 5 caracteres
        usuario.setPassword("12345");

        // Verificamos que el sistema lance la excepción esperada
        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            usuario.registrar(); // Este método dispara la validación interna
        });

        // Validamos que el mensaje sea el que definimos en la entidad
        assertEquals(Usuario.ERR_PASSWORD_CORTA, excepcion.getMessage());
    }

}
