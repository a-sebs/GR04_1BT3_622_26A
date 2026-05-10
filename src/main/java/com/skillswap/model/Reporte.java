package com.skillswap.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * HU1 - Entidad Reporte (version minima para TAREA 1.3).
 * Solo expone los metodos que ReporteService y sus tests utilizan.
 * Tu compañero completara el resto (campos JPA, getters, etc.).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reporte {

    private static final int MAX_DESCRIPCION = 250;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;
    private String descripcion;
    private Long idUsuarioReportante;
    private Long idUsuarioReportado;



    /**
     * Criterio HU1: rechaza si la descripcion supera 250 caracteres
     * o el motivo esta vacio.
     */
    public void validar() {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El motivo del reporte no puede estar vacio.");
        }
        if (descripcion != null && descripcion.length() > MAX_DESCRIPCION) {
            throw new IllegalArgumentException(
                    "La descripcion no puede superar los " + MAX_DESCRIPCION + " caracteres.");
        }
    }


}
