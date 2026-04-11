package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "usuario_solicitante_id", nullable = false)
	private Usuario usuarioSolicitante;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "usuario_match_id", nullable = false)
	private Usuario usuarioMatch;

	@Column(nullable = false)
	private Float compatibilidad;

	@Column(nullable = false, length = 20)
	private String estado;

	@Column(nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@PrePersist
	void onCreate() {
		if (estado == null || estado.isBlank()) {
			estado = "GENERADO";
		}
		if (fechaCreacion == null) {
			fechaCreacion = LocalDateTime.now();
		}
	}

	public void confirmar() {
		estado = "CONFIRMADO";
	}

	public void rechazar() {
		estado = "RECHAZADO";
	}
}
