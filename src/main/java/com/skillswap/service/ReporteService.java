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
        // La validación principal está en el modelo Reporte (validar)
        reporte.validar();
        return reporteRepository.save(reporte);
    }

    @Transactional
    public Reporte crearReporte(Long reportanteId, Long reportadoId, String motivo, String descripcion) {
        if (reportanteId == null) {
            throw new IllegalArgumentException("El id del usuario reportante es obligatorio.");
        }
        if (reportadoId == null) {
            throw new IllegalArgumentException("El id del usuario reportado es obligatorio.");
        }

        Reporte reporte = new Reporte();
        reporte.setMotivo(motivo != null ? motivo.trim() : null);
        reporte.setDescripcion(descripcion);
        reporte.validar();

        Usuario reportante = usuarioRepository.findById(reportanteId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario reportante no existe."));
        Usuario reportado = usuarioRepository.findById(reportadoId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario reportado no existe."));

        reporte.setReportante(reportante);
        reporte.setReportado(reportado);
        return reporteRepository.save(reporte);
    }
}
