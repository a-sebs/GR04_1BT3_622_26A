package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reportes")
public class Reporte {
public static final int MAX_DESCRIPCION = 250;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    @Column(nullable = false)
    private Long reportadoId;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(nullable = false, length = 250)
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
}

