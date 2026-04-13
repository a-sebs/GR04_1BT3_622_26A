package com.skillswap.service;

import com.skillswap.model.PerfilHabilidades;
import com.skillswap.repository.PerfilHabilidadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class AlgoritmoMatching {

	private Float umbralMinimo = 0.1f;
	private String criterioOrden = "desc";

	@Autowired(required = false)
	private PerfilHabilidadesRepository perfilHabilidadesRepository;

	public float calcularCompatibilidad(PerfilHabilidades p1, PerfilHabilidades p2) {
		if (p1 == null || p2 == null) {
			return 0.0f;
		}

		Set<String> buscaP1 = p1.getHabilidadesBusca();
		Set<String> ofreceP1 = p1.getHabilidadesOfrece();
		Set<String> ofreceP2 = p2.getHabilidadesOfrece();
		Set<String> buscaP2 = p2.getHabilidadesBusca();

		long coincidenciasParaAprender = contarCoincidencias(buscaP1, ofreceP2);
		long coincidenciasReciprocas = contarCoincidencias(ofreceP1, buscaP2);

		int totalBase = Math.max(1, buscaP1.size() + ofreceP1.size());
		return (float) (coincidenciasParaAprender + coincidenciasReciprocas) / totalBase;
	}

	public List<PerfilHabilidades> consultarPerfiles() {
		if (perfilHabilidadesRepository == null) {
			return List.of();
		}
		return perfilHabilidadesRepository.findAll();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public List ordenarResultados(List lista) {
		if (lista == null || lista.isEmpty()) {
			return List.of();
		}

		if (lista.getFirst() instanceof ResultadoCompatibilidad) {
			List<ResultadoCompatibilidad> resultados = new ArrayList<>(lista);
			Comparator<ResultadoCompatibilidad> comparador = Comparator.comparing(ResultadoCompatibilidad::puntaje);
			if (!"asc".equalsIgnoreCase(criterioOrden)) {
				comparador = comparador.reversed();
			}
			resultados.sort(comparador);
			return resultados;
		}

		return lista;
	}

	@SuppressWarnings("rawtypes")
	public List<ResultadoCompatibilidad> calcularCompatibilidades(PerfilHabilidades perfilActual,
												  List<PerfilHabilidades> candidatos,
												  String filtroHabilidad) {
		List<ResultadoCompatibilidad> resultados = new ArrayList<>();
		for (PerfilHabilidades candidato : candidatos) {
			if (perfilActual.getUsuario().getId().equals(candidato.getUsuario().getId())) {
				continue;
			}
			if (!candidato.coincideConFiltro(filtroHabilidad)) {
				continue;
			}
			float puntaje = calcularCompatibilidad(perfilActual, candidato);
			if (umbralMinimo == null || puntaje >= umbralMinimo) {
				resultados.add(new ResultadoCompatibilidad(candidato, puntaje));
			}
		}
		List ordenados = ordenarResultados(resultados);
		List<ResultadoCompatibilidad> salida = new ArrayList<>();
		for (Object elemento : ordenados) {
			salida.add((ResultadoCompatibilidad) elemento);
		}
		return salida;
	}

	private long contarCoincidencias(Set<String> origen, Set<String> destino) {
		if (origen == null || destino == null || origen.isEmpty() || destino.isEmpty()) {
			return 0;
		}
		return origen.stream()
				.map(String::trim)
				.map(String::toLowerCase)
				.filter(h -> destino.stream().map(String::trim).map(String::toLowerCase).anyMatch(h::equals))
				.count();
	}

	public record ResultadoCompatibilidad(PerfilHabilidades perfil, float puntaje) {
	}
}
