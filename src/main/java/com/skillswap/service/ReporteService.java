package com.skillswap.service;

import com.skillswap.model.Reporte;
import com.skillswap.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {
    private final ReporteRepository reporteRepository;
    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }
    @Transactional
    public Reporte guardar(Reporte reporte) {
        reporte.validar();                        // delega validacion al modelo
        return reporteRepository.save(reporte);   // persiste la entidad
    }
}
