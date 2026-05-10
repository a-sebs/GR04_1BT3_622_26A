package com.skillswap.service;

import com.skillswap.model.Match;
import com.skillswap.model.PerfilHabilidades;
import com.skillswap.repository.BloqueoRepository;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.PerfilHabilidadesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final PerfilHabilidadesRepository perfilHabilidadesRepository;
    // R4
    private final BloqueoRepository bloqueoRepository;
    private final AlgoritmoMatching algoritmoMatching;
    // Inyección de dependencias
    public MatchService(MatchRepository matchRepository,
                        PerfilHabilidadesRepository perfilHabilidadesRepository,
                        BloqueoRepository bloqueoRepository,
                        AlgoritmoMatching algoritmoMatching) {
        this.matchRepository = matchRepository;
        this.perfilHabilidadesRepository = perfilHabilidadesRepository;
        // R4
        this.bloqueoRepository = bloqueoRepository;
        this.algoritmoMatching = algoritmoMatching;
    }
    @Transactional
    public List<Match> buscarYGuardarMatches(Long usuarioId, String filtroHabilidad, String filtroNombreUsuario) {
        PerfilHabilidades perfilActual = perfilHabilidadesRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalStateException("No existe perfil para el usuario en sesion."));
        List<PerfilHabilidades> candidatos = obtenerCandidatos(usuarioId, filtroNombreUsuario);
        List<AlgoritmoMatching.ResultadoCompatibilidad> puntajes = algoritmoMatching
                .calcularCompatibilidades(perfilActual, candidatos, filtroHabilidad);
        matchRepository.deleteByUsuarioSolicitanteId(usuarioId);
        List<Match> nuevosMatches = puntajes.stream().map(resultado -> construirMatch(perfilActual, resultado)).toList();
        matchRepository.saveAll(nuevosMatches);
        return matchRepository.findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(usuarioId);
    }

    // R4
    private List<PerfilHabilidades> obtenerCandidatos(Long usuarioId, String filtroNombreUsuario) {
        return algoritmoMatching.consultarPerfiles().stream()
                .filter(perfil -> perfil.getUsuario() != null)
                .filter(perfil -> perfil.getUsuario().getId() != null)
                .filter(perfil -> !perfil.getUsuario().getId().equals(usuarioId))
                .filter(perfil -> perfil.coincideConNombreUsuario(filtroNombreUsuario))
                .filter(perfil -> noEstaBloqueado(usuarioId, perfil.getUsuario().getId()))
                .toList();
    }

    // R4
    private boolean noEstaBloqueado(Long usuarioId, Long otroUsuarioId) {
        return !bloqueoRepository.existsByIdBloqueadorAndIdBloqueado(usuarioId, otroUsuarioId)
                && !bloqueoRepository.existsByIdBloqueadorAndIdBloqueado(otroUsuarioId, usuarioId);
    }

    // R4
    private Match construirMatch(PerfilHabilidades perfilActual, AlgoritmoMatching.ResultadoCompatibilidad resultado) {
        Match match = new Match();
        match.setUsuarioSolicitante(perfilActual.getUsuario());
        match.setUsuarioMatch(resultado.perfil().getUsuario());
        match.setCompatibilidad(resultado.puntaje());
        match.setEstado("GENERADO");
        return match;
    }
    @Transactional(readOnly = true)
    public List<Match> obtenerMatchesDeUsuario(Long usuarioId) {
        return matchRepository.findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public Optional<Match> obtenerMatchDeUsuario(Long matchId, Long usuarioId) {
        return matchRepository.findByIdAndUsuarioSolicitanteId(matchId, usuarioId);
    }
}