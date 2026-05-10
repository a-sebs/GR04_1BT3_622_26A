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
@NoArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reportante", nullable = false)
    private Usuario reportante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reportado", nullable = false)
    private Usuario reportado;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(nullable = false, length = 500)
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
    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public Long getReportadoId() {
        return reportadoId;
    }

    public void setReportadoId(Long reportadoId) {
        this.reportadoId = reportadoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;
}

