package com.skillswap.service;

import com.skillswap.model.Reporte;
import com.skillswap.repository.ReporteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    private static final Long REPORTADO_ID = 42L;
    private static final String MOTIVO_VALIDO = "Contenido inapropiado";
    private static final String DESCRIPCION_VALIDA = "El usuario publica mensajes ofensivos en el perfil.";
    private static final String DESCRIPCION_MAYOR_A_250 =
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    @DisplayName("deberiaGuardarReporteCuandoLosDatosSonValidos")
    void deberiaGuardarReporteCuandoLosDatosSonValidos() {
        // Arrange
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<Reporte> reporteCaptor = ArgumentCaptor.forClass(Reporte.class);

        // Act
        Reporte resultado = reporteService.crearReporte(REPORTADO_ID, MOTIVO_VALIDO, DESCRIPCION_VALIDA);

        // Assert
        assertNotNull(resultado);
        assertEquals(REPORTADO_ID, resultado.getReportadoId());
        assertEquals(MOTIVO_VALIDO, resultado.getMotivo());
        assertEquals(DESCRIPCION_VALIDA, resultado.getDescripcion());
        verify(reporteRepository, times(1)).save(reporteCaptor.capture());

        Reporte reporteGuardado = reporteCaptor.getValue();
        assertEquals(REPORTADO_ID, reporteGuardado.getReportadoId());
        assertEquals(MOTIVO_VALIDO, reporteGuardado.getMotivo());
        assertEquals(DESCRIPCION_VALIDA, reporteGuardado.getDescripcion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("deberiaLanzarExcepcionCuandoElMotivoEsVacio")
    void deberiaLanzarExcepcionCuandoElMotivoEsVacio(String motivo) {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reporteService.crearReporte(REPORTADO_ID, motivo, DESCRIPCION_VALIDA));

        assertEquals("El motivo es obligatorio.", exception.getMessage());
        verify(reporteRepository, never()).save(any(Reporte.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {DESCRIPCION_MAYOR_A_250})
    @DisplayName("deberiaLanzarExcepcionCuandoLaDescripcionSupera250Caracteres")
    void deberiaLanzarExcepcionCuandoLaDescripcionSupera250Caracteres(String descripcion) {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reporteService.crearReporte(REPORTADO_ID, MOTIVO_VALIDO, descripcion));

        assertEquals("La descripcion no puede superar los 250 caracteres.", exception.getMessage());
        verify(reporteRepository, never()).save(any(Reporte.class));
    }
}
