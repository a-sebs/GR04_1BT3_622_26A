package com.skillswap.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidadorDatosTest {

	private final ValidadorDatos validador = new ValidadorDatos();

	// Válidos
	@ParameterizedTest
	@ValueSource(strings = { "usuario1", "User123", "ABC123", "test" })
	void nombreValido_retornaTrue(String nombre) {
		assertTrue(validador.esFormatoNombreValido(nombre));
	}

	// Inválidos
	@ParameterizedTest
	@ValueSource(strings = { "usuario-1", "user_1", "user@1", "usuario 1", "usuarioñ1" })
	void nombreInvalido_retornaFalse(String nombre) {
		assertFalse(validador.esFormatoNombreValido(nombre));
	}

	// Null y vacío
	@ParameterizedTest
	@NullAndEmptySource
	void nombreNuloOVacio_retornaFalse(String nombre) {
		assertFalse(validador.esFormatoNombreValido(nombre));
	}

	// Nombres largos (exceden límite)
	@ParameterizedTest
	@ValueSource(strings = { "a".repeat(51), "usuario123456789012345678901234567890123456789012" })
	void nombreMuyLargo_retornaFalse(String nombre) {
		assertFalse(validador.esFormatoNombreValido(nombre));
	}

	// Nombre en el límite (exacto)
	@ParameterizedTest
	@ValueSource(strings = { "a".repeat(50), "usuario1234567890123456789012345678901234567890" })
	void nombreEnLimite_retornaTrue(String nombre) {
		assertTrue(validador.esFormatoNombreValido(nombre));
	}
}

