package com.skillswap.service;

import com.skillswap.model.Reporte;
import com.skillswap.model.Usuario;
import com.skillswap.repository.ReporteRepository;
import com.skillswap.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {

    private static final int MAX_LONGITUD_DESCRIPCION = 250;

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteService(ReporteRepository reporteRepository, UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reporte crearReporte(Long reportanteId, Long reportadoId, String motivo, String descripcion) {
        validarReportanteId(reportanteId);
        validarReportadoId(reportadoId);
        validarMotivo(motivo);
        validarDescripcion(descripcion);

        Usuario reportante = usuarioRepository.findById(reportanteId)
            .orElseThrow(() -> new IllegalArgumentException("El usuario reportante no existe."));
        Usuario reportado = usuarioRepository.findById(reportadoId)
            .orElseThrow(() -> new IllegalArgumentException("El usuario reportado no existe."));

        Reporte reporte = new Reporte();
        reporte.setReportante(reportante);
        reporte.setReportado(reportado);
        reporte.setMotivo(motivo.trim());
        reporte.setDescripcion(descripcion);
        return reporteRepository.save(reporte);
    }

    private void validarReportanteId(Long reportanteId) {
        if (reportanteId == null) {
            throw new IllegalArgumentException("El id del usuario reportante es obligatorio.");
        }
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

