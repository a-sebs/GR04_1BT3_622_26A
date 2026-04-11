package com.skillswap.repository;

import com.skillswap.model.PerfilHabilidades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfilHabilidadesRepository extends JpaRepository<PerfilHabilidades, Long> {
	Optional<PerfilHabilidades> findByUsuarioId(Long usuarioId);
	List<PerfilHabilidades> findByUsuarioIdNot(Long usuarioId);
}
