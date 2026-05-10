package com.skillswap.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Getter
@Setter
// @NoArgsConstructor
public class Reporte {

    private static final int MAX_DESCRIPCION = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reportante_id", nullable = false)
    private Usuario reportante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reportado_id", nullable = false)
    private Usuario reportado;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(nullable = true, length = 500)
    private String descripcion;

    public Reporte() {
    }

    public void validar() {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo es obligatorio.");
        }
        if (descripcion != null && descripcion.length() > MAX_DESCRIPCION) {
            throw new IllegalArgumentException(
                    "La descripcion no puede superar los " + MAX_DESCRIPCION + " caracteres.");
        }
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;
}
