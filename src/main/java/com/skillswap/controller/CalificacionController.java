package com.skillswap.controller;

import com.skillswap.model.Calificacion;
import com.skillswap.model.Match;
import com.skillswap.model.Sesion;
import com.skillswap.repository.CalificacionRepository;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.SesionRepository;
import com.skillswap.service.ActualizadorPerfil;
import com.skillswap.service.ValidadorDatos;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/calificacion")
public class CalificacionController {

	private final CalificacionRepository calificacionRepository;
	private final SesionRepository sesionRepository;
	private final MatchRepository matchRepository;
	private final ActualizadorPerfil actualizadorPerfil;
	private final ValidadorDatos validadorDatos;

	public CalificacionController(CalificacionRepository calificacionRepository,
							 SesionRepository sesionRepository,
							 MatchRepository matchRepository,
							 ActualizadorPerfil actualizadorPerfil,
							 ValidadorDatos validadorDatos) {
		this.calificacionRepository = calificacionRepository;
		this.sesionRepository = sesionRepository;
		this.matchRepository = matchRepository;
		this.actualizadorPerfil = actualizadorPerfil;
		this.validadorDatos = validadorDatos;
	}

	@GetMapping("/sesiones-finalizadas")
	public String listarSesionesFinalizadas(Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		List<Sesion> sesionesFinalizadas = obtenerSesionesFinalizadasDelUsuario(usuarioId);
		model.addAttribute("sesionesFinalizadas", sesionesFinalizadas);
		return "calificacion/formularioCalificacion";
	}

	@GetMapping("/formulario/{idSesion}")
	public String registrarCalificacion(@PathVariable String idSesion, Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesionSeleccionada = sesionRepository.findById(idSesion).orElse(null);
		if (sesionSeleccionada == null || !sesionPerteneceAlUsuario(sesionSeleccionada, usuarioId)) {
			return "redirect:/calificacion/sesiones-finalizadas";
		}

		cargarFormulario(model, sesionSeleccionada, "", 0, null);
		model.addAttribute("sesionesFinalizadas", obtenerSesionesFinalizadasDelUsuario(usuarioId));
		return "calificacion/formularioCalificacion";
	}

	@PostMapping("/registrar")
	@Transactional
	public String actualizarPuntaje(@RequestParam("idSesion") String idSesion,
								@RequestParam("puntuacion") Integer puntuacion,
								@RequestParam("comentario") String comentario,
								Model model,
								HttpSession session,
								RedirectAttributes redirectAttributes) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Sesion sesion = sesionRepository.findById(idSesion).orElse(null);
		if (sesion == null || !sesionPerteneceAlUsuario(sesion, usuarioId)) {
			return "redirect:/calificacion/sesiones-finalizadas";
		}

		String error = validarDatosCalificacion(puntuacion, comentario);
		if (error != null) {
			cargarFormulario(model, sesion, comentario, puntuacion == null ? 0 : puntuacion, error);
			model.addAttribute("sesionesFinalizadas", obtenerSesionesFinalizadasDelUsuario(usuarioId));
			return "calificacion/formularioCalificacion";
		}

		String idEvaluador = String.valueOf(usuarioId);
		if (calificacionRepository.existsByIdSesionAndIdEvaluador(idSesion, idEvaluador)) {
			cargarFormulario(model, sesion, comentario, puntuacion, "Ya registraste una calificacion para esta sesion.");
			model.addAttribute("sesionesFinalizadas", obtenerSesionesFinalizadasDelUsuario(usuarioId));
			return "calificacion/formularioCalificacion";
		}

		Calificacion calificacion = new Calificacion();
		calificacion.setIdSesion(idSesion);
		calificacion.setIdEvaluador(idEvaluador);
		calificacion.setPuntuacion(puntuacion);
		calificacion.setComentario(comentario);
		calificacion.registrar();
		Calificacion guardada = calificacionRepository.save(calificacion);

		sesion.setEstado("CALIFICADA");
		sesionRepository.save(sesion);

		Long idUsuarioEvaluado = obtenerUsuarioEvaluado(sesion, usuarioId);
		actualizadorPerfil.actualizarPuntaje(String.valueOf(idUsuarioEvaluado), puntuacion);

		redirectAttributes.addFlashAttribute("mensaje", "Calificacion registrada correctamente.");
		return "redirect:/calificacion/resultado/" + guardada.getId();
	}

	@GetMapping("/resultado/{idCalificacion}")
	public String mostrarResultado(@PathVariable String idCalificacion, Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Calificacion calificacion = calificacionRepository.findById(idCalificacion).orElse(null);
		if (calificacion == null || !String.valueOf(usuarioId).equals(calificacion.getIdEvaluador())) {
			return "redirect:/calificacion/sesiones-finalizadas";
		}

		Sesion sesion = sesionRepository.findById(calificacion.getIdSesion()).orElse(null);
		if (sesion == null) {
			return "redirect:/calificacion/sesiones-finalizadas";
		}

		Long idUsuarioEvaluado = obtenerUsuarioEvaluado(sesion, usuarioId);
		Float reputacionActualizada = actualizadorPerfil.calcularPromedio(String.valueOf(idUsuarioEvaluado));

		model.addAttribute("calificacion", calificacion);
		model.addAttribute("reputacionActualizada", reputacionActualizada);
		model.addAttribute("idUsuarioEvaluado", idUsuarioEvaluado);
		return "calificacion/pantallaResultado";
	}

	private List<Sesion> obtenerSesionesFinalizadasDelUsuario(Long usuarioId) {
		List<String> idsMatch = matchRepository.findByUsuarioSolicitanteIdOrUsuarioMatchId(usuarioId, usuarioId).stream()
				.map(Match::getId)
				.filter(id -> id != null)
				.map(String::valueOf)
				.toList();
		if (idsMatch.isEmpty()) {
			return List.of();
		}
		return sesionRepository.findByIdMatchInAndEstadoIn(idsMatch, List.of("FINALIZADA"));
	}

	private boolean sesionPerteneceAlUsuario(Sesion sesion, Long usuarioId) {
		if (sesion == null || sesion.getIdMatch() == null) {
			return false;
		}
		Long idMatch;
		try {
			idMatch = Long.parseLong(sesion.getIdMatch());
		} catch (NumberFormatException e) {
			return false;
		}
		Match match = matchRepository.findById(idMatch).orElse(null);
		if (match == null) {
			return false;
		}
		return match.getUsuarioSolicitante().getId().equals(usuarioId)
				|| match.getUsuarioMatch().getId().equals(usuarioId);
	}

	private Long obtenerUsuarioEvaluado(Sesion sesion, Long idEvaluador) {
		Long idMatch = Long.parseLong(sesion.getIdMatch());
		Match match = matchRepository.findById(idMatch)
				.orElseThrow(() -> new IllegalStateException("No se encontro el match de la sesion."));
		if (match.getUsuarioSolicitante().getId().equals(idEvaluador)) {
			return match.getUsuarioMatch().getId();
		}
		return match.getUsuarioSolicitante().getId();
	}

	private String validarDatosCalificacion(Integer puntuacion, String comentario) {
		Map<String, Object> datos = Map.of(
				"puntuacion", puntuacion == null ? 0 : puntuacion,
				"comentario", comentario == null ? "" : comentario
		);
		if (validadorDatos.validar(datos)) {
			return null;
		}
		List<String> errores = validadorDatos.getMensajeError();
		return errores.isEmpty() ? "Error al validar la calificacion." : errores.getFirst();
	}

	private void cargarFormulario(Model model, Sesion sesion, String comentario, int puntuacion, String error) {
		model.addAttribute("sesionSeleccionada", sesion);
		model.addAttribute("comentario", comentario == null ? "" : comentario);
		model.addAttribute("puntuacion", puntuacion);
		model.addAttribute("error", error);
	}

	private Long obtenerUsuarioEnSesion(HttpSession session) {
		Object usuarioId = session.getAttribute("usuarioId");
		if (usuarioId instanceof Number valorNumerico) {
			return valorNumerico.longValue();
		}
		return null;
	}
}
