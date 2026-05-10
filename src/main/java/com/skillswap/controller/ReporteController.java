package com.skillswap.controller;

import com.skillswap.service.ReporteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping("/reporte/guardar")
    public String guardarReporte(
            @RequestParam("motivo") String motivo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("idUsuarioReportado") Long idUsuarioReportado,
            RedirectAttributes redirectAttributes) {

        try {
            reporteService.crearReporte(idUsuarioReportado, motivo, descripcion);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito", "Reporte enviado correctamente.");
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/reporte/nuevo";
        }
    }
}
