package com.skillswap.service;

import com.skillswap.model.Reporte;
import com.skillswap.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {

    private static final int MAX_LONGITUD_DESCRIPCION = 250;

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Transactional
    public Reporte crearReporte(Long reportadoId, String motivo, String descripcion) {
        validarReportadoId(reportadoId);
        validarMotivo(motivo);
        validarDescripcion(descripcion);

        Reporte reporte = new Reporte();
        reporte.setReportadoId(reportadoId);
        reporte.setMotivo(motivo.trim());
        reporte.setDescripcion(descripcion);
        return reporteRepository.save(reporte);
    }

    private void validarReportadoId(Long reportadoId) {
        if (reportadoId == null) {
            throw new IllegalArgumentException("El id del usuario reportado es obligatorio.");
        }
    }

    private void validarMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo es obligatorio.");
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (descripcion.length() > MAX_LONGITUD_DESCRIPCION) {
            throw new IllegalArgumentException("La descripcion no puede superar los 250 caracteres.");
        }
    }
}

