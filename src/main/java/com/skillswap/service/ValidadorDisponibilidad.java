package com.skillswap.service;

import com.skillswap.model.Match;
import com.skillswap.model.Sesion;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.SesionRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidadorDisponibilidad {

	private final MatchRepository matchRepository;
	private final SesionRepository sesionRepository;
	private List<LocalDateTime> horariosOcupados = new ArrayList<>();

	public ValidadorDisponibilidad(MatchRepository matchRepository, SesionRepository sesionRepository) {
		this.matchRepository = matchRepository;
		this.sesionRepository = sesionRepository;
	}

	public boolean verificar(Long idU1, Long idU2, LocalDate fecha, String hora) {
		if (idU1 == null || idU2 == null || fecha == null || hora == null || hora.isBlank()) {
			return false;
		}

		LocalTime horaSeleccionada;
		try {
			horaSeleccionada = LocalTime.parse(hora);
		} catch (Exception e) {
			return false;
		}

		if (LocalDateTime.of(fecha, horaSeleccionada).isBefore(LocalDateTime.now())) {
			return false;
		}

		List<String> estadosBloqueantes = List.of("PENDIENTE", "CONFIRMADA");
		Date fechaSql = Date.valueOf(fecha);

		boolean solicitanteOcupado = existeCruceParaUsuario(idU1, fechaSql, hora, estadosBloqueantes);
		boolean destinatarioOcupado = existeCruceParaUsuario(idU2, fechaSql, hora, estadosBloqueantes);

		return !solicitanteOcupado && !destinatarioOcupado;
	}

	public List<LocalDateTime> obtenerHorarios(Long idUsuario) {
		if (idUsuario == null) {
			horariosOcupados = List.of();
			return horariosOcupados;
		}

		List<String> estadosBloqueantes = List.of("PENDIENTE", "CONFIRMADA");
		List<String> idsMatch = obtenerIdsMatchDeUsuario(idUsuario);
		if (idsMatch.isEmpty()) {
			horariosOcupados = List.of();
			return horariosOcupados;
		}

		horariosOcupados = sesionRepository.findByIdMatchInAndEstadoIn(idsMatch, estadosBloqueantes).stream()
				.filter(sesion -> estadosBloqueantes.contains(sesion.getEstado()))
				.map(sesion -> LocalDateTime.of(convertirFecha(sesion.getFecha()), LocalTime.parse(sesion.getHora())))
				.sorted(Comparator.naturalOrder())
				.toList();

		return horariosOcupados;
	}

	private boolean existeCruceParaUsuario(Long idUsuario, Date fecha, String hora, List<String> estadosBloqueantes) {
		List<String> idsMatch = obtenerIdsMatchDeUsuario(idUsuario);
		for (String idMatch : idsMatch) {
			if (sesionRepository.existsByIdMatchAndFechaAndHoraAndEstadoIn(idMatch, fecha, hora, estadosBloqueantes)) {
				return true;
			}
		}
		return false;
	}

	private List<String> obtenerIdsMatchDeUsuario(Long idUsuario) {
		return matchRepository.findByUsuarioSolicitanteIdOrUsuarioMatchId(idUsuario, idUsuario).stream()
				.map(Match::getId)
				.filter(id -> id != null)
				.map(String::valueOf)
				.collect(Collectors.toList());
	}

	private LocalDate convertirFecha(java.util.Date fecha) {
		return new java.sql.Date(fecha.getTime()).toLocalDate();
	}
}
