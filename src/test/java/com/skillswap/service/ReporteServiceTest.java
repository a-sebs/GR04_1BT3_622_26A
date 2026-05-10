package com.skillswap.service;

import com.skillswap.model.Reporte;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReporteServiceTest {
    private static final Long ID_REPORTANTE = 1L;
    private static final Long ID_REPORTADO  = 2L;
    private static final String MOTIVO_VALIDO = "Comportamiento toxico";
    private static final String DESCRIPCION_VALIDA = "Insultos reiterados durante la sesion.";

    /**
     * Construye un reporte con datos validos por defecto.
     * Cada test sobreescribe SOLO el campo que quiere invalidar.
     */
    private Reporte construirReporteValido() {
        Reporte reporte = new Reporte();
        reporte.setMotivo(MOTIVO_VALIDO);
        reporte.setDescripcion(DESCRIPCION_VALIDA);
        reporte.setIdUsuarioReportante(ID_REPORTANTE);
        reporte.setIdUsuarioReportado(ID_REPORTADO);
        return reporte;
    }

    @Test
    @DisplayName("HU1 - Debe rechazar el reporte cuando la descripcion supera los 250 caracteres")
    void given_descripcion_mayor_a_250_caracteres_when_guardar_then_lanza_excepcion() {
        // Given
        ReporteService reporteService = new ReporteService(null);
        Reporte reporte = construirReporteValido();
        reporte.setDescripcion("a".repeat(251));

        // When / Then
        assertThrows(
                IllegalArgumentException.class,
                () -> reporteService.guardar(reporte),
                "Se esperaba que el servicio rechazara descripciones mayores a 250 caracteres."
        );
    }

    @Test
    @DisplayName("HU1 - Debe rechazar el reporte cuando el motivo esta vacio")
    void given_motivo_vacio_when_guardar_then_lanza_excepcion() {
        // Given
        ReporteService reporteService = new ReporteService(null);
        Reporte reporte = construirReporteValido();
        reporte.setMotivo("");

        // When / Then
        assertThrows(
                IllegalArgumentException.class,
                () -> reporteService.guardar(reporte),
                "Se esperaba que el servicio rechazara reportes con motivo vacio."
        );
    }
}
