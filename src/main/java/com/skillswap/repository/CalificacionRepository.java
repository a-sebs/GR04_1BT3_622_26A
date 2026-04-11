package com.skillswap.repository;

import com.skillswap.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, String> {
	boolean existsByIdSesionAndIdEvaluador(String idSesion, String idEvaluador);
	List<Calificacion> findByIdSesion(String idSesion);
}
