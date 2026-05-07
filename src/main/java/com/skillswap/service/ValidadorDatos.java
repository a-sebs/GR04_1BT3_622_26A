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
	public static final String ERR_NOMBRE_OBLIGATORIO = "El nombre es obligatorio.";
	public static final String ERR_NOMBRE_FORMATO = "Error al validar el nombre de usuario, no ingrese caracteres especiales";
	public static final String ERR_PASSWORD_OBLIGATORIA = "La contraseña es obligatoria.";
	public static final String ERR_PASSWORD_CORTA = "La contraseña tiene 8 caracteres mínimo";
	public static final String ERR_CORREO_OBLIGATORIO = "El correo es obligatorio.";
	public static final String ERR_CORREO_FORMATO = "El correo debe seguir el formato: correo@dominio.com";
	private final List<String> mensajesError = new ArrayList<>();
	private final List<String> reglasActivas = new ArrayList<>();


	public boolean validar(Map<String, Object> datos) {
		mensajesError.clear();
		reglasActivas.clear();

		if (datos.containsKey("nombre")) {
			reglasActivas.add("nombreUsuario");
			String nombre = (String) datos.get("nombre");
			String error = obtenerErrorNombre(nombre);
			if (error != null) {
				mensajesError.add(error);
			}
		}

		if (datos.containsKey("password")) {
			reglasActivas.add("password");
			String password = (String) datos.get("password");
			String error = obtenerErrorPassword(password);
			if (error != null) {
				mensajesError.add(error);
			}
		}

		if (datos.containsKey("correo")) {
			reglasActivas.add("correo");
			String correo = (String) datos.get("correo");
			String error = obtenerErrorCorreo(correo);
			if (error != null) {
				mensajesError.add(error);
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

	public List<String> validarCredencialesUsuario(String nombre, String password, String correo) {
		List<String> errores = new ArrayList<>();
		String errorNombre = obtenerErrorNombre(nombre);
		if (errorNombre != null) {
			errores.add(errorNombre);
		}
		String errorPassword = obtenerErrorPassword(password);
		if (errorPassword != null) {
			errores.add(errorPassword);
		}
		String errorCorreo = obtenerErrorCorreo(correo);
		if (errorCorreo != null) {
			errores.add(errorCorreo);
		}
		return errores;
	}

	public List<String> validarActualizacionPerfil(String nombre, String correo) {
		List<String> errores = new ArrayList<>();
		if (nombre != null && !nombre.isBlank()) {
			String errorNombre = obtenerErrorNombre(nombre);
			if (errorNombre != null) {
				errores.add(errorNombre);
			}
		}
		if (correo != null && !correo.isBlank()) {
			String errorCorreo = obtenerErrorCorreo(correo);
			if (errorCorreo != null) {
				errores.add(errorCorreo);
			}
		}
		return errores;
	}

	public boolean validarNombre(String nombre) {
		return obtenerErrorNombre(nombre) == null;
	}

	public boolean validarEmail(String email) {
		return obtenerErrorCorreo(email) == null;
	}

	public boolean validarPassword(String pwd) {
		return obtenerErrorPassword(pwd) == null;
	}

	public List<String> getMensajeError() {
		return List.copyOf(mensajesError);
	}

	public List<String> getReglasActivas() {
		return List.copyOf(reglasActivas);
	}

	private String obtenerErrorNombre(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.isBlank()) {
			return ERR_NOMBRE_OBLIGATORIO;
		}
		if (!USERNAME_PATTERN.matcher(nombreUsuario.trim()).matches()) {
			return ERR_NOMBRE_FORMATO;
		}
		return null;
	}

	private String obtenerErrorPassword(String password) {
		if (password == null || password.isBlank()) {
			return ERR_PASSWORD_OBLIGATORIA;
		}
		if (password.length() < 8) {
			return ERR_PASSWORD_CORTA;
		}
		return null;
	}

	private String obtenerErrorCorreo(String correo) {
		if (correo == null || correo.isBlank()) {
			return ERR_CORREO_OBLIGATORIO;
		}
		if (!EMAIL_PATTERN.matcher(correo.trim()).matches()) {
			return ERR_CORREO_FORMATO;
		}
		return null;
	}

	private String validarSeleccionHabilidades(List<String> habilidadesDomina, List<String> habilidadesAprender) {
		if (habilidadesDomina == null || habilidadesDomina.isEmpty()) {
			return "Debe seleccionar al menos una habilidad en cada área.";
		}
		if (habilidadesAprender == null || habilidadesAprender.isEmpty()) {
			return "Debe seleccionar al menos una habilidad en cada área.";
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
}
