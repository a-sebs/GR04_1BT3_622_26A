package com.skillswap.controller;

import com.skillswap.model.Match;
import com.skillswap.boundary.DetallesSesion;
import com.skillswap.model.Sesion;
import com.skillswap.model.Usuario;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.SesionRepository;
import com.skillswap.repository.UsuarioRepository;
import com.skillswap.service.ValidadorDisponibilidad;
import com.skillswap.service.ValidadorDatos;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class SesionController {

	private final UsuarioRepository usuarioRepository;
	private final MatchRepository matchRepository;
	private final SesionRepository sesionRepository;
	private final ValidadorDisponibilidad validadorDisponibilidad;
	private final ValidadorDatos validadorDatos;

	public SesionController(UsuarioRepository usuarioRepository,
						 MatchRepository matchRepository,
						 SesionRepository sesionRepository,
						 ValidadorDisponibilidad validadorDisponibilidad,
						 ValidadorDatos validadorDatos) {
		this.usuarioRepository = usuarioRepository;
		this.matchRepository = matchRepository;
		this.sesionRepository = sesionRepository;
		this.validadorDisponibilidad = validadorDisponibilidad;
		this.validadorDatos = validadorDatos;
	}

	@GetMapping("/")
	public String inicio() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String mostrarLogin(Model model) {
		if (!model.containsAttribute("mensaje")) {
			model.addAttribute("mensaje", null);
		}
		if (!model.containsAttribute("error")) {
			model.addAttribute("error", null);
		}
		return "sesion/pantallaMatch";
	}

	@PostMapping("/login")
	public String iniciarSesion(@RequestParam("nombre") String nombre,
							 @RequestParam("password") String password,
							 HttpSession session,
							 RedirectAttributes redirectAttributes) {
		var usuario = usuarioRepository.findByNombreIgnoreCaseAndPassword(nombre.trim(), password).orElse(null);
		if (usuario == null) {
			redirectAttributes.addFlashAttribute("error", "Credenciales inválidas.");
			return "redirect:/login";
		}
		session.setAttribute("usuarioId", usuario.getId());
		session.setAttribute("usuarioNombre", usuario.getNombre());
		redirectAttributes.addFlashAttribute("mensaje", "Bienvenido " + nombre.trim() + ".");
		return "redirect:/sesion/confirmada";
	}

	@GetMapping("/sesion/confirmada")
	public String sesionConfirmada(HttpSession session, Model model) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}
		model.addAttribute("usuarioId", usuarioId);
		return "sesion/sesionConfirmada";
	}

	@GetMapping("/sesion/agenda/{matchId}")
	public String agendarSesion(@PathVariable Long matchId, Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Match match = matchRepository.findByIdAndUsuarioSolicitanteId(matchId, usuarioId).orElse(null);
		if (match == null) {
			return "redirect:/match/lista";
		}

		cargarAgenda(model, match, "", "", "", null);
		return "sesion/pantallaAgenda";
	}

	@PostMapping("/sesion/agenda/{matchId}")
	@Transactional
	public String confirmarSesion(@PathVariable Long matchId,
						  @RequestParam("fecha") String fecha,
						  @RequestParam("hora") String hora,
						  @RequestParam("mensaje") String mensaje,
						  Model model,
						  HttpSession session,
						  RedirectAttributes redirectAttributes) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Match match = matchRepository.findByIdAndUsuarioSolicitanteId(matchId, usuarioId).orElse(null);
		if (match == null) {
			return "redirect:/match/lista";
		}

		LocalDate fechaSeleccionada;
		try {
			fechaSeleccionada = LocalDate.parse(fecha);
		} catch (Exception e) {
			cargarAgenda(model, match, fecha, hora, mensaje, "La fecha seleccionada no es válida.");
			return "sesion/pantallaAgenda";
		}

		if (!verificarDisponibilidad(match, fechaSeleccionada, hora)) {
			cargarAgenda(model, match, fecha, hora, mensaje, "El horario no está disponible. Seleccione otra fecha u hora.");
			return "sesion/pantallaAgenda";
		}

		Sesion nuevaSesion = new Sesion();
		nuevaSesion.setIdMatch(String.valueOf(match.getId()));
		nuevaSesion.setFecha(Date.from(fechaSeleccionada.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		nuevaSesion.setHora(hora);
		nuevaSesion.agendar();

		Sesion sesionGuardada = sesionRepository.save(nuevaSesion);

		redirectAttributes.addFlashAttribute("mensaje",
				"Solicitud enviada con éxito. La sesión quedó registrada en estado Pendiente.");
		redirectAttributes.addFlashAttribute("mensajeIntroduccion", mensaje);
		return "redirect:/sesion/confirmada/" + sesionGuardada.getId();
	}

	@GetMapping("/sesion/confirmada/{sesionId}")
	public String mostrarConfirmacion(@PathVariable String sesionId, Model model, HttpSession session) {
		return presentarDetallesSesion(sesionId, model, session);
	}

	public String presentarDetallesSesion(String sesionId, Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesion = obtenerDetallesSesion(sesionId);
		if (sesion == null) {
			return "redirect:/match/lista";
		}

		Match match = obtenerMatchDesdeSesion(sesion);
		if (match == null || !match.getUsuarioSolicitante().getId().equals(usuarioId)) {
			return "redirect:/match/lista";
		}

		DetallesSesion detallesSesion = construirDetallesSesion(sesion);

		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("sesionAgendada", sesion);
		model.addAttribute("detallesSesion", detallesSesion);
		model.addAttribute("destinatarioNombre", match.getUsuarioMatch().getNombre());
		return "sesion/sesionConfirmada";
	}

	@PostMapping("/sesion/finalizar/{sesionId}")
	@Transactional
	public String finalizarSesion(@PathVariable String sesionId,
						  HttpSession session,
						  RedirectAttributes redirectAttributes) {
		return aceptarSesion(sesionId, session, redirectAttributes);
	}

	// Método sobrecargado para lógica de negocio pura (usado por tests y lógica interna)
	public void aceptarSesion(String sesionId) {
		if (sesionId == null || sesionId.isBlank()) {
			throw new IllegalArgumentException("El ID de sesión no puede estar vacío.");
		}

		Sesion sesion = sesionRepository.findById(sesionId)
				.orElseThrow(() -> new IllegalArgumentException("La sesión no existe."));

		if (!validarReglasNegocio(sesion)) {
			throw new IllegalStateException("La sesión no cumple las reglas de negocio.");
		}

		sesion.finalizar();
		actualizarBaseDeDatos(sesion);
	}

	// Método para confirmar decisión pendiente (usado por tests)
	public void confirmarDesision() {
		// Lógica para confirmar decisión - puede ser extendido según requieran los tests
		throw new IllegalStateException("No hay decisión pendiente para confirmar.");
	}

	// Método original con parámetros HTTP (usado por controlador web)
	public String aceptarSesion(String sesionId,
					 HttpSession session,
					 RedirectAttributes redirectAttributes) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesion = obtenerDetallesSesion(sesionId);
		if (sesion == null) {
			return "redirect:/match/lista";
		}

		Match match = obtenerMatchDesdeSesion(sesion);
		if (match == null || !match.getUsuarioSolicitante().getId().equals(usuarioId)) {
			return "redirect:/match/lista";
		}

		if (!validarReglasNegocio(sesion)) {
			redirectAttributes.addFlashAttribute("error", "La sesion no cumple las reglas de negocio.");
			return "redirect:/sesion/confirmada/" + sesionId;
		}

		DetallesSesion detallesSesion = construirDetallesSesion(sesion);
		detallesSesion.seleccionarOpcionAceptar();
		detallesSesion.confirmarAccion();
		detallesSesion.confirmarDecision();

		sesion.finalizar();
		actualizarBaseDeDatos(sesion);
		redirectAttributes.addFlashAttribute("mensaje", detallesSesion.mostrarMensajeExito());
		return "redirect:/sesion/confirmada/" + sesionId;
	}

	public Sesion obtenerDetallesSesion(String sesionId) {
		Sesion sesion = sesionRepository.findById(sesionId).orElse(null);
		if (sesion == null) {
			return null;
		}
		return sesion;
	}

	public boolean validarReglasNegocio(Sesion sesion) {
		return sesion != null && sesion.validarReglas();
	}

	public void actualizarBaseDeDatos(Sesion sesion) {
		sesion.actualizar();
		sesionRepository.save(sesion);
	}

	public boolean verificarDisponibilidad(Match match, LocalDate fecha, String hora) {
		return validadorDisponibilidad.verificar(
				match.getUsuarioSolicitante().getId(),
				match.getUsuarioMatch().getId(),
				fecha,
				hora
		);
	}

	private void cargarAgenda(Model model, Match match, String fecha, String hora, String mensaje, String error) {
		List<LocalDateTime> horarios = validadorDisponibilidad.obtenerHorarios(match.getUsuarioMatch().getId());
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		model.addAttribute("matchSeleccionado", match);
		model.addAttribute("fecha", fecha);
		model.addAttribute("hora", hora);
		model.addAttribute("mensajeTexto", mensaje);
		model.addAttribute("error", error);
		model.addAttribute("horariosOcupados", horarios.stream().map(h -> h.format(formato)).toList());
	}

	private Long obtenerUsuarioEnSesion(HttpSession session) {
		Object usuarioId = session.getAttribute("usuarioId");
		if (usuarioId instanceof Number valorNumerico) {
			return valorNumerico.longValue();
		}
		return null;
	}

	private Match obtenerMatchDesdeSesion(Sesion sesion) {
		if (sesion == null) {
			return null;
		}

		String idMatchTexto = sesion.getIdMatch();
		if (idMatchTexto == null || idMatchTexto.isBlank()) {
			return null;
		}

		Long idMatch = parsearIdMatch(idMatchTexto);
		if (idMatch == null) {
			return null;
		}

		return matchRepository.findById(idMatch).orElse(null);
	}

	private DetallesSesion construirDetallesSesion(Sesion sesion) {
		DetallesSesion detallesSesion = new DetallesSesion();
		detallesSesion.desplegarDetallesSesion(sesion.obtenerDetalles());
		return detallesSesion;
	}

	private Long parsearIdMatch(String idMatchTexto) {
		try {
			return Long.parseLong(idMatchTexto);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// ========== MÉTODO PARA PROCESAR SOLICITUD DE SESIÓN ==========

	/**
	 * Procesa una solicitud de sesión, actualizando su estado a ACEPTADA o RECHAZADA.
	 * 
	 * @param sesionId ID de la sesión a procesar
	 * @param aceptada true para ACEPTADA, false para RECHAZADA
	 */
	@Transactional
	public void procesarSolicitud(String sesionId, boolean aceptada) {
		if (sesionId == null || sesionId.isBlank()) {
			return;
		}
		
		sesionRepository.findById(sesionId).ifPresent(sesion -> {
			String nuevoEstado = aceptada ? "ACEPTADA" : "RECHAZADA";
			sesion.setEstado(nuevoEstado);
			sesionRepository.save(sesion);
		});
	}

	@PostMapping("/recuperar")
	public String solicitarRecuperacion(@RequestParam("correo") String correo, RedirectAttributes ra) {
		if (!procesarRecuperacion(correo)) {
			ra.addFlashAttribute("error", "El usuario no existe.");
			return "redirect:/login/olvido";
		}

		Long id = usuarioRepository.findByCorreoIgnoreCase(correo).get().getId();
		return "redirect:/login/restablecer/" + id;
	}

	@GetMapping("/login/olvido")
	public String mostrarOlvidoPassword() {
		return "sesion/olvidoPassword";
	}

	@PostMapping("/login/verificar")
	public String verificarCorreo(@RequestParam("correo") String correo,
								  RedirectAttributes redirectAttributes) {
		Optional<Usuario> usuario = usuarioRepository.findByCorreoIgnoreCase(correo);
		if (usuario.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
			return "redirect:/login/olvido";
		}
		return "redirect:/login/restablecer/" + usuario.get().getId();
	}

	@GetMapping("/login/restablecer/{id}")
	public String mostrarActualizarPassword(@PathVariable Long id, Model model) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		if (usuario == null) {
			return "redirect:/login";
		}
		model.addAttribute("usuarioId", id);
		return "sesion/actualizarPassword";
	}

	@PostMapping("/login/guardar-password")
	@Transactional
	public String guardarPassword(@RequestParam("usuarioId") Long usuarioId,
								  @RequestParam("password") String nuevaPassword,
								  Model model,
								  RedirectAttributes redirectAttributes) {
		Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
		if (usuario == null) {
			model.addAttribute("error", "Usuario no encontrado.");
			return "redirect:/login";
		}

		// Validar longitud de contraseña (CA3: Mínimo 8 caracteres)
		if (!validadorDatos.validarPassword(nuevaPassword)) {
			model.addAttribute("usuarioId", usuarioId);
			model.addAttribute("error", ValidadorDatos.ERR_PASSWORD_CORTA);
			return "sesion/actualizarPassword";
		}

		// Actualizar contraseña: null en nombre y correo para no modificarlos
		usuario.actualizarDatos(null, nuevaPassword, null);
		usuarioRepository.save(usuario);

		redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada exitosamente.");
		return "redirect:/login";
	}

	@Transactional(readOnly = true)
	public boolean procesarRecuperacion(String correo) {
		return usuarioRepository.findByCorreoIgnoreCase(correo).isPresent();
	}
}
