package com.skillswap.service;

import com.skillswap.model.Reporte;
import com.skillswap.model.Usuario;
import com.skillswap.repository.ReporteRepository;
import com.skillswap.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {
    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteService(ReporteRepository reporteRepository, UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reporte guardar(Reporte reporte) {
        reporte.validar(); // delega validacion al modelo
        return reporteRepository.save(reporte); // persiste la entidad
    }

    @Transactional
    public Reporte crearReporte(Long reportanteId, Long reportadoId, String motivo, String descripcion) {
        validarReportanteId(reportanteId);
        validarReportadoId(reportadoId);
        validarMotivo(motivo);

        // Construir y validar el reporte (validaciones de negocio en la entidad) antes de consultar usuarios
        Reporte reporte = new Reporte();
        reporte.setMotivo(motivo.trim());
        reporte.setDescripcion(descripcion != null ? descripcion.trim() : null);
        reporte.validar();

        Usuario reportante = usuarioRepository.findById(reportanteId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario reportante no existe."));
        Usuario reportado = usuarioRepository.findById(reportadoId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario reportado no existe."));

        reporte.setReportante(reportante);
        reporte.setReportado(reportado);
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
}
