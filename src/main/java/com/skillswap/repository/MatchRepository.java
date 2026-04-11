package com.skillswap.repository;

import com.skillswap.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
	List<Match> findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(Long usuarioSolicitanteId);
	Optional<Match> findByIdAndUsuarioSolicitanteId(Long id, Long usuarioSolicitanteId);
	void deleteByUsuarioSolicitanteId(Long usuarioSolicitanteId);
}
