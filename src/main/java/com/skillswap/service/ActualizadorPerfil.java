package com.skillswap.service;

import com.skillswap.model.Calificacion;
import com.skillswap.model.Match;
import com.skillswap.model.PerfilHabilidades;
import com.skillswap.model.Sesion;
import com.skillswap.repository.CalificacionRepository;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.PerfilHabilidadesRepository;
import com.skillswap.repository.SesionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActualizadorPerfil {

	private Float pesoCalificacion = 1.0f;
	private final CalificacionRepository calificacionRepository;
	private final SesionRepository sesionRepository;
	private final MatchRepository matchRepository;
	private final PerfilHabilidadesRepository perfilHabilidadesRepository;

	public ActualizadorPerfil(CalificacionRepository calificacionRepository,
							 SesionRepository sesionRepository,
							 MatchRepository matchRepository,
							 PerfilHabilidadesRepository perfilHabilidadesRepository) {
		this.calificacionRepository = calificacionRepository;
		this.sesionRepository = sesionRepository;
		this.matchRepository = matchRepository;
		this.perfilHabilidadesRepository = perfilHabilidadesRepository;
	}

	@Transactional
	public void actualizarPuntaje(String idUsuario, int puntuacion) {
		if (idUsuario == null || idUsuario.isBlank()) {
			throw new IllegalArgumentException("El id de usuario evaluado es obligatorio.");
		}
		if (puntuacion < 1 || puntuacion > 5) {
			throw new IllegalArgumentException("La puntuacion debe estar entre 1 y 5.");
		}

		Float promedioActualizado = calcularPromedio(idUsuario);
		Long idUsuarioLong = convertirAIdLong(idUsuario);
		PerfilHabilidades perfil = perfilHabilidadesRepository.findByUsuarioId(idUsuarioLong).orElse(null);
		if (perfil != null) {
			perfil.setReputacion(promedioActualizado);
			perfilHabilidadesRepository.save(perfil);
		}
	}

	@Transactional(readOnly = true)
	public Float calcularPromedio(String idUsuario) {
		List<Calificacion> historial = obtenerHistorial(idUsuario);
		if (historial.isEmpty()) {
			return 0.0f;
		}

		float sumaPonderada = 0.0f;
		float sumaPesos = 0.0f;
		for (Calificacion registro : historial) {
			sumaPonderada += registro.getPuntuacion() * pesoCalificacion;
			sumaPesos += pesoCalificacion;
		}
		return sumaPesos == 0.0f ? 0.0f : (sumaPonderada / sumaPesos);
	}

	@Transactional(readOnly = true)
	public List<Calificacion> obtenerHistorial(String idUsuario) {
		Long idUsuarioLong = convertirAIdLong(idUsuario);
		List<Calificacion> registros = calificacionRepository.findAll();
		if (registros.isEmpty()) {
			return List.of();
		}

		Map<String, Sesion> sesionesPorId = sesionRepository.findAllById(
				registros.stream().map(Calificacion::getIdSesion).collect(Collectors.toSet())
		).stream().collect(Collectors.toMap(Sesion::getId, sesion -> sesion));

		Set<Long> idsMatch = sesionesPorId.values().stream()
				.map(Sesion::getIdMatch)
				.filter(this::esNumero)
				.map(Long::parseLong)
				.collect(Collectors.toSet());
		Map<Long, Match> matchesPorId = new HashMap<>();
		for (Match match : matchRepository.findAllById(idsMatch)) {
			matchesPorId.put(match.getId(), match);
		}

		return registros.stream()
				.filter(calificacion -> perteneceAUsuarioEvaluado(calificacion, idUsuarioLong, sesionesPorId, matchesPorId))
				.toList();
	}

	private boolean perteneceAUsuarioEvaluado(Calificacion calificacion,
										 Long idUsuarioEvaluado,
										 Map<String, Sesion> sesionesPorId,
										 Map<Long, Match> matchesPorId) {
		Sesion sesion = sesionesPorId.get(calificacion.getIdSesion());
		if (sesion == null || !esNumero(sesion.getIdMatch()) || !esNumero(calificacion.getIdEvaluador())) {
			return false;
		}

		Match match = matchesPorId.get(Long.parseLong(sesion.getIdMatch()));
		if (match == null || match.getUsuarioSolicitante() == null || match.getUsuarioMatch() == null) {
			return false;
		}

		long idEvaluador = Long.parseLong(calificacion.getIdEvaluador());
		Long evaluado = match.getUsuarioSolicitante().getId().equals(idEvaluador)
				? match.getUsuarioMatch().getId()
				: match.getUsuarioSolicitante().getId();
		return evaluado.equals(idUsuarioEvaluado);
	}

	private Long convertirAIdLong(String idUsuario) {
		if (!esNumero(idUsuario)) {
			throw new IllegalArgumentException("El id de usuario no es valido.");
		}
		return Long.parseLong(idUsuario);
	}

	private boolean esNumero(String valor) {
		if (valor == null || valor.isBlank()) {
			return false;
		}
		for (char c : valor.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

}
