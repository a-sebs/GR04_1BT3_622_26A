package com.skillswap.service;

import com.skillswap.model.Match;
import com.skillswap.model.PerfilHabilidades;
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
    private final AlgoritmoMatching algoritmoMatching;
    // Inyección de dependencias
    public MatchService(MatchRepository matchRepository,
                        PerfilHabilidadesRepository perfilHabilidadesRepository,
                        AlgoritmoMatching algoritmoMatching) {
        this.matchRepository = matchRepository;
        this.perfilHabilidadesRepository = perfilHabilidadesRepository;
        this.algoritmoMatching = algoritmoMatching;
    }
    @Transactional
    public List<Match> buscarYGuardarMatches(Long usuarioId, String filtroHabilidad, String filtroNombreUsuario) {
        PerfilHabilidades perfilActual = perfilHabilidadesRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalStateException("No existe perfil para el usuario en sesion."));
        List<PerfilHabilidades> candidatos = algoritmoMatching.consultarPerfiles().stream()
                .filter(perfil -> perfil.getUsuario() != null)
                .filter(perfil -> perfil.getUsuario().getId() != null)
                .filter(perfil -> !perfil.getUsuario().getId().equals(usuarioId))
                .filter(perfil -> perfil.coincideConNombreUsuario(filtroNombreUsuario))
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
    @Transactional(readOnly = true)
    public List<Match> obtenerMatchesDeUsuario(Long usuarioId) {
        return matchRepository.findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public Optional<Match> obtenerMatchDeUsuario(Long matchId, Long usuarioId) {
        return matchRepository.findByIdAndUsuarioSolicitanteId(matchId, usuarioId);
    }
}