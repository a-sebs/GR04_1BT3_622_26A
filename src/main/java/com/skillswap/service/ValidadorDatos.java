package com.skillswap.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ValidadorDatos {

	private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	private final List<String> mensajesError = new ArrayList<>();
	private final List<String> reglasActivas = new ArrayList<>();

	public boolean validar(Map<String, Object> datos) {
		mensajesError.clear();
		reglasActivas.clear();

		if (datos.containsKey("nombre")) {
			reglasActivas.add("nombreUsuario");
			String nombre = (String) datos.get("nombre");
			String error = validarUsuarioFormato(nombre);
			if (error != null) {
				mensajesError.add(error);
			}
		}

		if (datos.containsKey("password")) {
			reglasActivas.add("password");
			String password = (String) datos.get("password");
			if (password == null || password.isBlank()) {
				mensajesError.add("La contraseña es obligatoria.");
			} else if (!validarPassword(password)) {
				mensajesError.add("La contraseña tiene 8 caracteres mínimo");
			}
		}

		if (datos.containsKey("correo")) {
			reglasActivas.add("correo");
			String correo = (String) datos.get("correo");
			if (correo == null || correo.isBlank()) {
				mensajesError.add("El correo es obligatorio.");
			} else if (!validarEmail(correo)) {
				mensajesError.add("El correo debe seguir el formato: correo@dominio.com");
			}
		}

		if (datos.containsKey("habilidadesDomina") || datos.containsKey("habilidadesAprender")
				|| datos.containsKey("habilidadesOfrece") || datos.containsKey("habilidadesBusca")) {
			reglasActivas.add("habilidades");
			@SuppressWarnings("unchecked")
			List<String> habilidadesDomina = (List<String>) (datos.containsKey("habilidadesDomina")
					? datos.get("habilidadesDomina")
					: datos.get("habilidadesOfrece"));
			@SuppressWarnings("unchecked")
			List<String> habilidadesAprender = (List<String>) (datos.containsKey("habilidadesAprender")
					? datos.get("habilidadesAprender")
					: datos.get("habilidadesBusca"));
			String error = validarSeleccionHabilidades(habilidadesDomina, habilidadesAprender);
			if (error != null) {
				mensajesError.add(error);
			}
		}

		return mensajesError.isEmpty();
	}

	public boolean validarEmail(String email) {
		if (email == null || email.isBlank()) {
			return false;
		}
		return EMAIL_PATTERN.matcher(email.trim()).matches();
	}

	public boolean validarPassword(String pwd) {
		if (pwd == null || pwd.isBlank()) {
			return false;
		}
		return pwd.length() >= 8;
	}

	public List<String> getMensajeError() {
		return List.copyOf(mensajesError);
	}

	public List<String> getReglasActivas() {
		return List.copyOf(reglasActivas);
	}

	private String validarUsuarioFormato(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.isBlank()) {
			return "El nombre es obligatorio.";
		}
		if (!USERNAME_PATTERN.matcher(nombreUsuario.trim()).matches()) {
			return "Error al validar el nombre de usuario, no ingrese caracteres especiales";
		}
		return null;
	}

	private String validarSeleccionHabilidades(List<String> habilidadesDomina, List<String> habilidadesAprender) {
		if (habilidadesDomina == null || habilidadesDomina.isEmpty()) {
			return "Debe seleccionar al menos una habilidad para enseñar.";
		}
		if (habilidadesAprender == null || habilidadesAprender.isEmpty()) {
			return "Debe seleccionar al menos una habilidad para aprender.";
		}
		return null;
	}
}
