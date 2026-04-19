package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "sesiones")
@Getter
@Setter
@NoArgsConstructor
public class Sesion {

	@Id
	@Column(nullable = false, length = 40)
	private String id;

	@Column(nullable = false)
	private String idMatch;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date fecha;

	@Column(nullable = false, length = 5)
	private String hora;

	@Column(nullable = false, length = 20)
	private String estado;

	@PrePersist
	void prePersist() {
		if (id == null || id.isBlank()) {
			id = UUID.randomUUID().toString();
		}
		if (estado == null || estado.isBlank()) {
			estado = "PENDIENTE";
		}
	}

	public Sesion obtenerDetalles() {
		return this;
	}

	public boolean validarReglas() {
		if (idMatch == null || idMatch.isBlank()) {
			return false;
		}
		if (fecha == null) {
			return false;
		}
		if (hora == null || hora.isBlank()) {
			return false;
		}
		return estado != null && !estado.isBlank();
	}

	public void actualizar() {
		if (!validarReglas()) {
			throw new IllegalStateException("La sesion no cumple las reglas de negocio.");
		}
	}

	public void agendar() {
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha es obligatoria.");
		}
		if (hora == null || hora.isBlank()) {
			throw new IllegalArgumentException("La hora es obligatoria.");
		}
		if (idMatch == null || idMatch.isBlank()) {
			throw new IllegalArgumentException("El id del match es obligatorio.");
		}
		estado = "PENDIENTE";
		actualizar();
	}

	public void cancelar() {
		estado = "CANCELADA";
		actualizar();
	}

	public void finalizar() {
		if ("CANCELADA".equalsIgnoreCase(estado)) {
			throw new IllegalStateException("No se puede finalizar una sesion cancelada.");
		}
		estado = "FINALIZADA";
		actualizar();
	}
}
