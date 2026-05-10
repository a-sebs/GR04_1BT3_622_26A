package com.skillswap.service;

import com.skillswap.model.Reporte;
import com.skillswap.model.Usuario;
import com.skillswap.repository.ReporteRepository;
import com.skillswap.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    private static final Long REPORTANTE_ID = 1L;
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

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    @DisplayName("deberiaGuardarReporteCuandoLosDatosSonValidos")
    void deberiaGuardarReporteCuandoLosDatosSonValidos() {
        // Arrange
        Usuario usuarioReportante = new Usuario();
        usuarioReportante.setId(REPORTANTE_ID);
        usuarioReportante.setNombre("Usuario Reportante");

        Usuario usuarioReportado = new Usuario();
        usuarioReportado.setId(REPORTADO_ID);
        usuarioReportado.setNombre("Usuario Reportado");

        when(usuarioRepository.findById(REPORTANTE_ID)).thenReturn(Optional.of(usuarioReportante));
        when(usuarioRepository.findById(REPORTADO_ID)).thenReturn(Optional.of(usuarioReportado));
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<Reporte> reporteCaptor = ArgumentCaptor.forClass(Reporte.class);

        // Act
        Reporte resultado = reporteService.crearReporte(REPORTANTE_ID, REPORTADO_ID, MOTIVO_VALIDO, DESCRIPCION_VALIDA);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioReportante, resultado.getReportante());
        assertEquals(usuarioReportado, resultado.getReportado());
        assertEquals(MOTIVO_VALIDO, resultado.getMotivo());
        assertEquals(DESCRIPCION_VALIDA, resultado.getDescripcion());
        verify(reporteRepository, times(1)).save(reporteCaptor.capture());

        Reporte reporteGuardado = reporteCaptor.getValue();
        assertEquals(usuarioReportante, reporteGuardado.getReportante());
        assertEquals(usuarioReportado, reporteGuardado.getReportado());
        assertEquals(MOTIVO_VALIDO, reporteGuardado.getMotivo());
        assertEquals(DESCRIPCION_VALIDA, reporteGuardado.getDescripcion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("deberiaLanzarExcepcionCuandoElMotivoEsVacio")
    void deberiaLanzarExcepcionCuandoElMotivoEsVacio(String motivo) {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reporteService.crearReporte(REPORTANTE_ID, REPORTADO_ID, motivo, DESCRIPCION_VALIDA));

        assertEquals("El motivo es obligatorio.", exception.getMessage());
        verify(reporteRepository, never()).save(any(Reporte.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {DESCRIPCION_MAYOR_A_250})
    @DisplayName("deberiaLanzarExcepcionCuandoLaDescripcionSupera250Caracteres")
    void deberiaLanzarExcepcionCuandoLaDescripcionSupera250Caracteres(String descripcion) {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reporteService.crearReporte(REPORTANTE_ID, REPORTADO_ID, MOTIVO_VALIDO, descripcion));

        assertEquals("La descripcion no puede superar los 250 caracteres.", exception.getMessage());
        verify(reporteRepository, never()).save(any(Reporte.class));
    }
}
