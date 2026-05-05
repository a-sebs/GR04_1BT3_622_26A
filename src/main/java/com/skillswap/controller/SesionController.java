package com.skillswap.controller;

import com.skillswap.model.Match;
import com.skillswap.model.Sesion;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.SesionRepository;
import com.skillswap.repository.UsuarioRepository;
import com.skillswap.service.ValidadorDisponibilidad;
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

@Controller
public class SesionController {

	private final UsuarioRepository usuarioRepository;
	private final MatchRepository matchRepository;
	private final SesionRepository sesionRepository;
	private final ValidadorDisponibilidad validadorDisponibilidad;

	private String sesionPendienteConfirmacionId;
	private String decisionPendiente;

	public SesionController(UsuarioRepository usuarioRepository,
						 MatchRepository matchRepository,
						 SesionRepository sesionRepository,
						 ValidadorDisponibilidad validadorDisponibilidad) {
		this.usuarioRepository = usuarioRepository;
		this.matchRepository = matchRepository;
		this.sesionRepository = sesionRepository;
		this.validadorDisponibilidad = validadorDisponibilidad;
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
	public String sesionConfirmada() {
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
		redirectAttributes.addFlashAttribute("notificacion",
				"Se notificó a " + match.getUsuarioMatch().getNombre() + " sobre la nueva solicitud.");
		redirectAttributes.addFlashAttribute("mensajeIntroduccion", mensaje);
		return "redirect:/sesion/confirmada/" + sesionGuardada.getId();
	}

	@GetMapping("/sesion/confirmada/{sesionId}")
	public String mostrarConfirmacion(@PathVariable String sesionId, Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesion = sesionRepository.findById(sesionId).orElse(null);
		if (sesion == null) {
			return "redirect:/match/lista";
		}

		Match match = obtenerMatchDesdeSesion(sesion);
		if (match == null || !match.getUsuarioSolicitante().getId().equals(usuarioId)) {
			return "redirect:/match/lista";
		}

		model.addAttribute("sesionAgendada", sesion);
		model.addAttribute("destinatarioNombre", match.getUsuarioMatch().getNombre());
		return "sesion/sesionConfirmada";
	}

	@PostMapping("/sesion/finalizar/{sesionId}")
	@Transactional
	public String finalizarSesion(@PathVariable String sesionId,
						  HttpSession session,
						  RedirectAttributes redirectAttributes) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesion = sesionRepository.findById(sesionId).orElse(null);
		if (sesion == null) {
			return "redirect:/match/lista";
		}

		Match match = obtenerMatchDesdeSesion(sesion);
		if (match == null || !match.getUsuarioSolicitante().getId().equals(usuarioId)) {
			return "redirect:/match/lista";
		}

		sesion.finalizar();
		sesionRepository.save(sesion);
		redirectAttributes.addFlashAttribute("mensaje", "Sesion finalizada correctamente.");
		return "redirect:/sesion/confirmada/" + sesionId;
	}

	public void aceptarSesion(String id) {
		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("El id de la sesion es obligatorio.");
		}

		Sesion sesion = sesionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No existe una sesion con el id proporcionado."));

		validarVigencia(sesion);
		validarPermisos(sesion);

		sesion.setEstado("CONFIRMADA");
		sesionRepository.save(sesion);

		registrarTrazabilidad("ACEPTAR", id);
		notificarExito("Sesion aceptada correctamente para id: " + id);

		this.sesionPendienteConfirmacionId = id;
		this.decisionPendiente = "ACEPTAR";
	}

	// Compatibilidad con pruebas legacy tras merge.
	public String aceptarSesion(String id, HttpSession session, RedirectAttributes redirectAttributes) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesion = sesionRepository.findById(id).orElse(null);
		if (sesion == null) {
			return "redirect:/match/lista";
		}

		Match match = obtenerMatchDesdeSesion(sesion);
		if (match == null || match.getUsuarioSolicitante() == null
				|| !match.getUsuarioSolicitante().getId().equals(usuarioId)) {
			return "redirect:/match/lista";
		}

		sesion.setEstado("ACEPTADA");
		sesionRepository.save(sesion);

		if (redirectAttributes != null) {
			redirectAttributes.addFlashAttribute("mensaje", "Sesion aceptada correctamente.");
		}

		return "redirect:/sesion/confirmada/" + sesion.getId();
	}

	// Compatibilidad con pruebas legacy tras merge.
	public void procesarSolicitud(String sesionId, Boolean decision) {
		Sesion sesion = sesionRepository.findById(sesionId)
				.orElseThrow(() -> new IllegalArgumentException("No existe una sesion con el id proporcionado."));

		sesion.setEstado(Boolean.TRUE.equals(decision) ? "ACEPTADA" : "RECHAZADA");
		sesionRepository.save(sesion);
	}

	public void confirmarDesision() {
		if (sesionPendienteConfirmacionId == null || decisionPendiente == null) {
			throw new IllegalStateException("No hay una decision pendiente por confirmar.");
		}

		registrarTrazabilidad("CONFIRMAR_" + decisionPendiente, sesionPendienteConfirmacionId);
		notificarExito("Decision confirmada correctamente para sesion: " + sesionPendienteConfirmacionId);

		sesionPendienteConfirmacionId = null;
		decisionPendiente = null;
	}

	private void validarVigencia(Sesion sesion) {
		if (sesion.getFecha() == null) {
			throw new IllegalStateException("La sesion no tiene fecha definida.");
		}

		LocalDate fechaSesion = sesion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (fechaSesion.isBefore(LocalDate.now())) {
			throw new IllegalStateException("La sesion no esta vigente.");
		}
	}

	private void validarPermisos(Sesion sesion) {
		if (sesion.getEstado() == null || !"PENDIENTE".equalsIgnoreCase(sesion.getEstado())) {
			throw new IllegalStateException("La sesion no se puede aceptar en el estado actual.");
		}
	}

	private void registrarTrazabilidad(String accion, String sesionId) {
		System.out.println("TRAZABILIDAD - accion: " + accion + ", sesionId: " + sesionId);
	}

	private void notificarExito(String mensaje) {
		System.out.println("NOTIFICACION - " + mensaje);
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

	private Long parsearIdMatch(String idMatchTexto) {
		try {
			return Long.parseLong(idMatchTexto);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
