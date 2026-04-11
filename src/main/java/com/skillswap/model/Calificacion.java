package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "calificaciones")
@Getter
@Setter
@NoArgsConstructor
public class Calificacion {

	@Id
	@Column(nullable = false, length = 40)
	private String id;

	@Column(nullable = false, length = 40)
	private String idSesion;

	@Column(nullable = false, length = 40)
	private String idEvaluador;

	@Column(nullable = false)
	private Integer puntuacion;

	@Column(nullable = false, length = 300)
	private String comentario;

	@PrePersist
	void prePersist() {
		if (id == null || id.isBlank()) {
			id = UUID.randomUUID().toString();
		}
	}

	public void registrar() {
		if (idSesion == null || idSesion.isBlank()) {
			throw new IllegalArgumentException("La sesion evaluada es obligatoria.");
		}
		if (idEvaluador == null || idEvaluador.isBlank()) {
			throw new IllegalArgumentException("El evaluador es obligatorio.");
		}
		if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
			throw new IllegalArgumentException("La puntuacion debe estar entre 1 y 5.");
		}
		if (comentario == null || comentario.isBlank()) {
			throw new IllegalArgumentException("El comentario es obligatorio.");
		}
		comentario = comentario.trim();
	}
}
