package com.skillswap.repository;

import com.skillswap.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, String> {
	List<Sesion> findByIdMatch(String idMatch);
	boolean existsByIdMatchAndFechaAndHoraAndEstadoIn(String idMatch, Date fecha, String hora, List<String> estados);
	List<Sesion> findByIdMatchInAndEstadoIn(List<String> idMatches, List<String> estados);
}
