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
	private static final Pattern FILTRO_HABILIDAD_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s+#.-]{0,100}$");
	private static final Pattern COMENTARIO_PATTERN = Pattern.compile("^[A-Za-z\\s]+$");
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

		if (datos.containsKey("filtroHabilidad")) {
			reglasActivas.add("filtroHabilidad");
			String filtro = (String) datos.get("filtroHabilidad");
			String error = validarFiltroHabilidad(filtro);
			if (error != null) {
				mensajesError.add(error);
			}
		}

		if (datos.containsKey("puntuacion")) {
			reglasActivas.add("puntuacion");
			Object valor = datos.get("puntuacion");
			String error = validarPuntuacion(valor);
			if (error != null) {
				mensajesError.add(error);
			}
		}

		if (datos.containsKey("comentario")) {
			reglasActivas.add("comentario");
			String comentario = (String) datos.get("comentario");
			String error = validarComentario(comentario);
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

	private String validarFiltroHabilidad(String filtro) {
		if (filtro == null || filtro.isBlank()) {
			return null;
		}
		if (!FILTRO_HABILIDAD_PATTERN.matcher(filtro.trim()).matches()) {
			return "El filtro de busqueda contiene caracteres no permitidos.";
		}
		return null;
	}

	private String validarPuntuacion(Object valor) {
		if (!(valor instanceof Integer puntuacion)) {
			return "La puntuacion debe ser numerica.";
		}
		if (puntuacion < 1 || puntuacion > 5) {
			return "La puntuacion debe estar entre 1 y 5.";
		}
		return null;
	}

	private String validarComentario(String comentario) {
		if (comentario == null || comentario.isBlank()) {
			return "El comentario es obligatorio.";
		}
		if (!COMENTARIO_PATTERN.matcher(comentario.trim()).matches()) {
			return "Los comentarios tienen caracteres alfabéticos y no contienen caracteres especiales";
		}
		return null;
	}
	// En ValidadorDatos.java
	public boolean validarContrasena(String password) {
		if (password == null || password.length() < 8) {
			// Centralizamos el mensaje del CA3
			return false;
		}
		return true;
	}
}
