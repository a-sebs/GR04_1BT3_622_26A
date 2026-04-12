package com.skillswap.controller;

import com.skillswap.model.Match;
import com.skillswap.model.PerfilHabilidades;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.PerfilHabilidadesRepository;
import com.skillswap.service.AlgoritmoMatching;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/match")
public class MatchController {

	private static final List<String> CATALOGO_HABILIDADES = List.of(
			"Java", "Python", "JavaScript", "SQL", "Spring Boot", "HTML", "CSS", "Git"
	);

	private final MatchRepository matchRepository;
	private final PerfilHabilidadesRepository perfilHabilidadesRepository;
	private final AlgoritmoMatching algoritmoMatching;
	private final ValidadorDatos validadorDatos;

	public MatchController(MatchRepository matchRepository,
						   PerfilHabilidadesRepository perfilHabilidadesRepository,
						   AlgoritmoMatching algoritmoMatching,
						   ValidadorDatos validadorDatos) {
		this.matchRepository = matchRepository;
		this.perfilHabilidadesRepository = perfilHabilidadesRepository;
		this.algoritmoMatching = algoritmoMatching;
		this.validadorDatos = validadorDatos;
	}

	@GetMapping("/explorar")
	public String explorarMatches(Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		if (perfilHabilidadesRepository.findByUsuarioId(usuarioId).isEmpty()) {
			model.addAttribute("error", "Debe completar su perfil de habilidades para explorar matches.");
			return "redirect:/registro/perfil/" + usuarioId;
		}

		cargarDatosBusqueda(model, usuarioId, "");
		return "match/pantallaBusqueda";
	}

	@PostMapping("/buscar")
	@Transactional
	public String buscarMatches(@RequestParam(value = "filtroHabilidad", required = false) String filtroHabilidad,
							  Model model,
							  HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		if (!validadorDatos.validar(Map.of("filtroHabilidad", filtroHabilidad == null ? "" : filtroHabilidad))) {
			cargarDatosBusqueda(model, usuarioId, filtroHabilidad);
			model.addAttribute("error", validadorDatos.getMensajeError().getFirst());
			return "match/pantallaBusqueda";
		}

		List<Match> resultados = calcularCompatibilidad(usuarioId, filtroHabilidad);
		model.addAttribute("matches", resultados);
		model.addAttribute("filtroHabilidad", filtroHabilidad == null ? "" : filtroHabilidad);
		if (resultados.isEmpty()) {
			model.addAttribute("mensaje", "No se encontraron matches con los criterios ingresados.");
		}
		return "match/listaMatches";
	}

	@GetMapping("/lista")
	public String mostrarResultados(Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}
		List<Match> resultados = matchRepository.findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(usuarioId);
		model.addAttribute("matches", resultados);
		return "match/listaMatches";
	}

	@GetMapping("/detalle/{matchId}")
	public String mostrarDetalleDelPerfil(@PathVariable Long matchId,
								 Model model,
								 HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}

		Match match = matchRepository.findByIdAndUsuarioSolicitanteId(matchId, usuarioId).orElse(null);
		if (match == null) {
			return "redirect:/match/lista";
		}

		PerfilHabilidades perfilDetalle = perfilHabilidadesRepository
				.findByUsuarioId(match.getUsuarioMatch().getId())
				.orElse(null);

		model.addAttribute("matchSeleccionado", match);
		model.addAttribute("perfilDetalle", perfilDetalle);
		return "match/detallePerfil";
	}

	private List<Match> calcularCompatibilidad(Long usuarioId, String filtroHabilidad) {
		PerfilHabilidades perfilActual = perfilHabilidadesRepository.findByUsuarioId(usuarioId)
				.orElseThrow(() -> new IllegalStateException("No existe perfil para el usuario en sesion."));

		List<PerfilHabilidades> candidatos = algoritmoMatching.consultarPerfiles().stream()
				.filter(perfil -> perfil.getUsuario() != null)
				.filter(perfil -> perfil.getUsuario().getId() != null)
				.filter(perfil -> !perfil.getUsuario().getId().equals(usuarioId))
				.toList();
		List<AlgoritmoMatching.ResultadoCompatibilidad> puntajes = algoritmoMatching
				.calcularCompatibilidades(perfilActual, candidatos, filtroHabilidad);

		matchRepository.deleteByUsuarioSolicitanteId(usuarioId);

		List<Match> nuevosMatches = puntajes.stream().map(resultado -> {
			Match match = new Match();
			match.setUsuarioSolicitante(perfilActual.getUsuario());
			match.setUsuarioMatch(resultado.perfil().getUsuario());
			match.setCompatibilidad(resultado.puntaje());
			match.setEstado("GENERADO");
			return match;
		}).toList();

		matchRepository.saveAll(nuevosMatches);
		return matchRepository.findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(usuarioId);
	}

	private void cargarDatosBusqueda(Model model, Long usuarioId, String filtro) {
		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("catalogoHabilidades", CATALOGO_HABILIDADES);
		model.addAttribute("filtroHabilidad", filtro == null ? "" : filtro);
	}

	private Long obtenerUsuarioEnSesion(HttpSession session) {
		Object usuarioId = session.getAttribute("usuarioId");
		if (usuarioId instanceof Number valorNumerico) {
			return valorNumerico.longValue();
		}
		return null;
	}
}
