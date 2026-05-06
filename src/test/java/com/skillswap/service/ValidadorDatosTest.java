package com.skillswap.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ValidadorDatosTest {

	private final ValidadorDatos validador = new ValidadorDatos();

	@ParameterizedTest(name = "Para el nombre ''{0}'' el resultado debe ser {1}")
	@MethodSource("proveerDatosParaValidarNombre")
	void validarFormatoDeNombre(String nombre, boolean resultadoEsperado) {
		// Act & Assert
		assertEquals(resultadoEsperado, validador.esFormatoNombreValido(nombre));
	}

	private static Stream<Arguments> proveerDatosParaValidarNombre() {
		return Stream.of(
				// Casos Válidos (Deberían retornar true)
				Arguments.of("usuario1", true),
				Arguments.of("User123", true),
				Arguments.of("ABC123", true),
				Arguments.of("test", true),

				// Casos Inválidos (Deberían retornar false)
				Arguments.of("usuario-1", false),
				Arguments.of("user_1", false),
				Arguments.of("user@1", false),
				Arguments.of("usuario 1", false),
				Arguments.of("usuarioñ1", false),

				// Casos Nulos y Vacíos (Deberían retornar false)
				Arguments.of(null, false),
				Arguments.of("", false)
		);
	}
}

