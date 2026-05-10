package com.skillswap.controller;

import com.skillswap.service.ReporteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import com.skillswap.model.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/reporte/formulario/{id}")
    public String mostrarFormularioReporte(@PathVariable("id") Long idUsuarioReportado, Model model) {
        Usuario usuarioReportado = new Usuario();
        usuarioReportado.setId(idUsuarioReportado);
        model.addAttribute("usuarioReportado", usuarioReportado);
        return "reporte/formularioReporte";
    }

    @PostMapping("/reporte/guardar")
    public String guardarReporte(
            @RequestParam("motivo") String motivo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("idUsuarioReportado") Long idUsuarioReportado,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long reportanteId = obtenerUsuarioEnSesion(session);
        if (reportanteId == null) {
            return "redirect:/login";
        }

        try {
            reporteService.crearReporte(reportanteId, idUsuarioReportado, motivo, descripcion);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito", "Reporte enviado correctamente.");
            return "redirect:/match/lista";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/reporte/formulario/" + idUsuarioReportado;
        }
    }

    private Long obtenerUsuarioEnSesion(HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId instanceof Number valorNumerico) {
            return valorNumerico.longValue();
        }
        return null;
    }
}
