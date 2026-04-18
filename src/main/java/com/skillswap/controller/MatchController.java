package com.skillswap.controller;

import com.skillswap.model.Match;
import com.skillswap.model.PerfilHabilidades;
import com.skillswap.repository.PerfilHabilidadesRepository;
import com.skillswap.service.MatchService;
import com.skillswap.service.ValidadorDatos;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
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
	private final MatchService matchService;
    private final PerfilHabilidadesRepository perfilHabilidadesRepository;
    private final ValidadorDatos validadorDatos;
	public MatchController(MatchService matchService,
                           PerfilHabilidadesRepository perfilHabilidadesRepository,
                           ValidadorDatos validadorDatos) {
		this.matchService = matchService;
        this.perfilHabilidadesRepository = perfilHabilidadesRepository;
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
		cargarDatosBusqueda(model, usuarioId, "", "");
		return "match/pantallaBusqueda";
	}
	@PostMapping("/buscar")
public String buscarMatches(@RequestParam(value = "filtroHabilidad", required = false) String filtroHabilidad,
                            @RequestParam(value = "filtroNombreUsuario", required = false) String filtroNombreUsuario,
                            Model model,
                            HttpSession session) {
    Long usuarioId = obtenerUsuarioEnSesion(session);
    if (usuarioId == null) {
        return "redirect:/login";
    }
    if (!validadorDatos.validar(Map.of("filtroHabilidad", filtroHabilidad == null ? "" : filtroHabilidad))) {
        cargarDatosBusqueda(model, usuarioId, filtroHabilidad, filtroNombreUsuario);
        model.addAttribute("error", validadorDatos.getMensajeError().getFirst());
        return "match/pantallaBusqueda";
    }
    List<Match> resultados = matchService.buscarYGuardarMatches(usuarioId, filtroHabilidad, filtroNombreUsuario);

    model.addAttribute("matches", resultados);
    model.addAttribute("filtroHabilidad", filtroHabilidad == null ? "" : filtroHabilidad);
    model.addAttribute("filtroNombreUsuario", filtroNombreUsuario == null ? "" : filtroNombreUsuario);
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
	List<Match> resultados = matchService.obtenerMatchesDeUsuario(usuarioId);
    model.addAttribute("matches", resultados);
    return "match/listaMatches";
}
	@GetMapping("/detalle/{matchId}")
	public String mostrarDetalleDelPerfil(@PathVariable Long matchId, Model model, HttpSession session) {
		Long usuarioId = obtenerUsuarioEnSesion(session);
		if (usuarioId == null) {
			return "redirect:/login";
		}
		Match match = matchService.obtenerMatchDeUsuario(matchId, usuarioId).orElse(null);
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
	private void cargarDatosBusqueda(Model model, Long usuarioId, String filtro, String filtroNombreUsuario) {
		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("catalogoHabilidades", CATALOGO_HABILIDADES);
		model.addAttribute("filtroHabilidad", filtro == null ? "" : filtro);
		model.addAttribute("filtroNombreUsuario", filtroNombreUsuario == null ? "" : filtroNombreUsuario);
		model.addAttribute("usuariosDisponibles", perfilHabilidadesRepository.findByUsuarioIdNot(usuarioId).stream()
				.filter(perfil -> perfil.getUsuario() != null)
				.map(perfil -> perfil.getUsuario().getNombre())
				.filter(nombre -> nombre != null && !nombre.isBlank())
				.distinct()
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toList());
	}
	private Long obtenerUsuarioEnSesion(HttpSession session) {
		Object usuarioId = session.getAttribute("usuarioId");
		if (usuarioId instanceof Number valorNumerico) {
			return valorNumerico.longValue();
		}
		return null;
	}
}
